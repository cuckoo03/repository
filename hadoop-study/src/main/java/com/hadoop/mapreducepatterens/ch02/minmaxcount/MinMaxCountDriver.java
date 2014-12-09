package com.hadoop.mapreducepatterens.ch02.minmaxcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * input
 * <row Id="1" PostId="1" Text="I'm not sure how to hide the message, but why don't you get a new battery? That's what I did. You can generally get them relatively inexpensively on Ebay." CreationDate="2010-07-28T19:13:23.853" UserId="10" />
 * outout
 * 사용자		최소값	최대값	카운트
 * 10	2010-07-28T19:13:23.853	2010-07-28T20:35:16.733	11
 * 104	2010-07-28T19:51:00.770	2010-07-28T21:14:01.373	4
 * 12	2010-07-28T19:28:31.073	2010-07-28T20:07:34.350	4
 * 중복된 값을 가지고 있는 레코드의 경우 수행되지 않는다.
 * @author cuckoo03
 *
 */
public class MinMaxCountDriver {
	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: MinMaxCountDriver <in> <out>");
			System.exit(2);
		}

		Job job = new Job(conf, "MinMaxCount");

		job.setJarByClass(MinMaxCountDriver.class);
		job.setMapperClass(MinMaxCountMapper.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setCombinerClass(MinMaxCountReduce.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));

		job.setReducerClass(MinMaxCountReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MinMaxCountTuple.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		job.waitForCompletion(true);
	}
}