package com.hadoop.doithadoop.ch07.index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class CreateESIndex {
	public static class MyMap extends Mapper<Text, Text, Text, Text> {
		private String baseUrl = "";

		@Override
		public void setup(Context context) {
			String[] hosts = context.getConfiguration().getStrings("ESSServer",
					"localhost");
			baseUrl = "http://" + hosts[0] + ":9200/wikipedia/doc/";
		}

		@Override
		public void map(Text key, Text value, Context context)
				throws IOException {
			String line = value.toString();
			line = line.replace("\\", "\\\\");
			line = line.replace("\"", "\\\"");

			URL url = new URL(baseUrl + key.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			try {
				conn.setDoOutput(true);
				conn.setRequestMethod("PUT");

				// write request content
				String content = "{ \"body\" : \" " + line + "\" }";
				OutputStreamWriter out = new OutputStreamWriter(
						conn.getOutputStream());
				out.write(content);
				out.close();

				// read response
				String inputLine = "", inputLines = "";
				BufferedReader input = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				while ((inputLine = input.readLine()) != null) {
					inputLines += inputLine;
				}

				// response check
				int httpResult = conn.getResponseCode();
				if (httpResult == HttpURLConnection.HTTP_OK) {
					
				}
				
				if (inputLines.indexOf("\"ok\":true") < 0) {
					context.getCounter("Stats", "Error Docs").increment(1);
				} else {
					context.getCounter("Stats", "Success").increment(1);
				}
				input.close();
				conn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "CeateESIndex");

		job.setJarByClass(CreateESIndex.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(MyMap.class);

		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setNumReduceTasks(0);

		job.getConfiguration().setStrings("ESSServer", args[2]);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);
	}
}