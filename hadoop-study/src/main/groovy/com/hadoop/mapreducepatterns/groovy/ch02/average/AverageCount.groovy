package com.hadoop.mapreducepatterns.groovy.ch02.average

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
import org.apache.hadoop.util.GenericOptionsParser

/**
 * 주어진 사용자 코멘트 목록에 대해 시간대별 코멘트 길이의 평균을 측정한다.
 * input
 * <row Id="1" PostId="1" Text="I'm not sure how to hide the message, but why don't you get a new battery? That's what I did. You can generally get them relatively inexpensively on Ebay." CreationDate="2010-07-28T19:13:23.853" UserId="10" />
 * output
 * 시간	카운트	평균
 * 0	107.0	128.64487
 * 1	82.0	125.63415
 * 2	67.0	141.59702
 * @author cuckoo03
 *
 */
class AverageCount {
	static main(args) {
		Configuration conf = new Configuration()
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs()
		if (otherArgs.length != 2) {
			println "Usage:AverageDriver <in> <out>"
			System.exit(2)
		}
		Job job = new Job(conf, "StackOverflow Average Comment Length")
		job.setJarByClass(AverageCount.class)
		job.setInputFormatClass(TextInputFormat.class)
		job.setMapperClass(AverageMapper.class)
		job.setCombinerClass(AverageReducer.class)
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]))

		job.setOutputFormatClass(TextOutputFormat.class)
		job.setOutputKeyClass(IntWritable.class)
		job.setOutputValueClass(CountAverageTuple.class)
		job.setReducerClass(AverageReducer.class)
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]))

		System.exit(job.waitForCompletion(true) ? 0 : 1)
	}
}