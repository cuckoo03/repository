package com.hadoop.doithadoop.ch07.join2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class JoinIDTitle2 {
	public static class MyMapper extends Mapper<Text, Text, Text, Text> {
		private Map<String, String> idFreqMap = new HashMap<>();
		private Path[] localFiles;

		@Override
		public void map(Text key, Text value, Context context)
				throws IOException, InterruptedException {
			if (idFreqMap.get(value.toString()) != null) {
				context.write(
						key,
						new Text(value + "\t" + idFreqMap.get(value.toString())));
				context.getCounter("Stats", "number of title+docid").increment(
						1);
			}
		}

		@Override
		protected void setup(Context context) {
			try {
				localFiles = DistributedCache.getLocalCacheFiles(context
						.getConfiguration());
				System.out.println("localFiles:" + localFiles);
				FileInputStream fis = new FileInputStream(
						localFiles[0].toString());
				DataInputStream dis = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						dis));

				String line;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
					String[] tokens = line.split("\\t");
					idFreqMap.put(tokens[0], tokens[1]);
				}
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("read from distributed cache:an exception");
			}
		}
	}

	public static void main(String[] args) throws IOException,
			URISyntaxException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = new Job(conf);

		String titleDocID = args[0];
		String docIDFreq = args[1];
		String outputDir = args[2];

		if (outputDir == null || titleDocID == null || docIDFreq == null) {
			throw new IllegalArgumentException("Missing Parameter");
		}

		// 분산캐시를 사용하려면 url에서 접근가능한 경로에 객체가 존재해야 하기 때문에 현재는 테스트 불가
		DistributedCache.addCacheFile(new URI(docIDFreq), conf);

		job.setJobName("Join ID and Title v2");
		job.setJarByClass(JoinIDTitle2.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(MyMapper.class);
		job.setReducerClass(Reducer.class);

		job.setInputFormatClass(KeyValueTextInputFormat.class);

		FileInputFormat.addInputPath(job, new Path(titleDocID));
		FileOutputFormat.setOutputPath(job, new Path(outputDir));

		job.waitForCompletion(true);
	}
}