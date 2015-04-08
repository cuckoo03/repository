package com.hadoop.log.index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.hadoop.log.RegExpUtil;

/**
 * 해당 일자의 로그를 ES에 인덱싱한다.
 * <p>
 * Index:날짜 type:web id:web+날짜+시간
 * </p>
 * <p>
 * input args:input file, es url<http://localhost:9200/index/type/>, output dir
 * </p>
 * 
 * @author cuckoo03
 *
 */
public class LogESIndex extends Configured implements Tool {
	public static class IndexMapper extends
			Mapper<LongWritable, Text, LongWritable, Text> {
		private static final Logger log = Logger.getLogger("LogESIndex");
		private static final Pattern p = Pattern.compile(RegExpUtil
				.getApacheLogRegex());
		private static final DateFormat sdf = new SimpleDateFormat(
				"yyyyMMddHHmmss Z", Locale.ENGLISH);
		private String baseUrl;
		private JSONObject json;

		@Override
		public void setup(Context context) {
			baseUrl = context.getConfiguration().get("esUrl");
			log.warn("baseUrl:" + baseUrl);
			System.out.println("baseurl:" + baseUrl);
		}

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException {
			String line = value.toString();
			
			Matcher matcher = p.matcher(line);
			if (!matcher.matches()) {
				return;
			}

			log.debug("map url:" + baseUrl);
			URL url = new URL(baseUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			try {
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");

				// write request data
				json = createJsonObject(new JSONObject(), matcher);
				String content = json.toJSONString();
				OutputStreamWriter out = new OutputStreamWriter(
						conn.getOutputStream());
				out.write(content);
				out.close();

				// /read response data
				String inputLine = "", inputLines = "";
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				while ((inputLine = br.readLine()) != null) {
					inputLines += inputLine;
				}

				int httpResult = conn.getResponseCode();
				if (httpResult != HttpURLConnection.HTTP_CREATED) {
					context.getCounter("Stats", "Error").increment(1);
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}

		}

		@SuppressWarnings("unchecked")
		public JSONObject createJsonObject(JSONObject json, Matcher matcher)
				throws ParseException {
			json.put("localhost", matcher.group(1));
			json.put("identd", matcher.group(2));
			json.put("userId", matcher.group(3));
			json.put("requestDate", matcher.group(4));
			json.put("requestInfO", matcher.group(5));
			json.put("status", matcher.group(6));
			json.put("size", matcher.group(7));

			return json;
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(args).getRemainingArgs();
		Job job = new Job(conf, "LogESIndex");
		job.setJarByClass(LogESIndex.class);

		job.getConfiguration().set("esUrl", otherArgs[1]);

		job.setMapperClass(LogESIndex.IndexMapper.class);
		job.setNumReduceTasks(0);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(NullOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[2]));
		FileSystem.get(conf).delete(new Path(otherArgs[2]), true);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("USage LogESIndx <input> <es url> <output>");
			System.exit(1);
		}
		LogESIndex esIndex = new LogESIndex();
		System.exit(esIndex.run(args));
	}
}