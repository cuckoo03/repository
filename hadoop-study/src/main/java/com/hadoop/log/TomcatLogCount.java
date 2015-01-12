package com.hadoop.log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 톰캣 로그 카운트 세기
 * 
 * @author cuckoo03
 *
 */
public class TomcatLogCount {
	public static class LogCountMapper extends
			Mapper<LongWritable, Text, Text, LogTuple> {
		private static final DateFormat sdf = new SimpleDateFormat(
				"dd/MMM/yyyy:hh:mm:ss");
		private static final Calendar cal = Calendar.getInstance();
		private Text keyText = new Text();
		private LogTuple valueOne = new LogTuple();

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String strValue = value.toString();
			String[] split = strValue.split("\\s");
			String localhost = split[0];
			String identd = split[1];
			String userid = split[2];
			String requestTime = split[3].replace("[", "").replace("\"", "");
			Date requestDate = null;
			try {
				requestDate = sdf.parse(requestTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String timezone = split[4].replace("]", "").replace("\"", "");
			String requestInfo = split[5] + " " + split[6] + " " + split[7]
					+ " " + split[8] + " " + split[9];

			valueOne.setLocalhost(localhost);
			valueOne.setIdentd(identd);
			valueOne.setUserId(userid);
			valueOne.setRequestDate(requestDate);
			valueOne.setTimezone(timezone);
			valueOne.setRequestInfo(requestInfo);
			keyText.set(requestTime.substring(0, requestTime.length() - 3));

			context.write(keyText, valueOne);
		}
	}

	public static class LogCountReducer extends
			Reducer<Text, LogTuple, Text, LongWritable> {
		private LogTuple sumValue = new LogTuple();

		@Override
		public void reduce(Text key, Iterable<LogTuple> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (LogTuple val : values) {
				sum += 1;
			}
			context.write(key, new LongWritable(sum));
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();

		if (otherArgs.length != 2) {
			System.err
					.println("Usage: TomcatLogCount <in dir filename> <outdir>");
			System.exit(1);
		}

		Job job = new Job(conf);

		job.setJarByClass(TomcatLogCount.class);
		job.setMapperClass(TomcatLogCount.LogCountMapper.class);
//		job.setReducerClass(TomcatLogCount.LogCountReducer.class);
		job.setReducerClass(Reducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LogTuple.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		TextInputFormat.addInputPath(job, new Path(otherArgs[0]));
		TextOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
		System.out.println(otherArgs[1] + " dir is deleted.");

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}