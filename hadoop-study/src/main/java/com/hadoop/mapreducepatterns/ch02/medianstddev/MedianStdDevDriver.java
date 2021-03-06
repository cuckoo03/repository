package com.hadoop.mapreducepatterns.ch02.medianstddev;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * output
 * 0	98.0	109.95113
 * 1	106.5	94.54159
 * 2	94.0	128.03647
 * 3	124.0	110.238495
 * @author cuckoo03
 *
 */
public class MedianStdDevDriver {
	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.out.println("Usage: MedianStdDevDriver <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "Comment length Median stddev By Hour");
		job.setJarByClass(MedianStdDevDriver.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setMapperClass(MedianStdDevMapper.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));

		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(MedianStdDevTuple.class);
		job.setReducerClass(MedianStdDevReducer.class);
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}