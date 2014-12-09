package com.hadoop.mapreducepatterns.groovy.ch02.reversedIndex

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser

/**
 * 사용자 코멘트에서 답변 게시물ID 집합에 대한 위키피디아 URL 역색인을 생성한다.
 * 
 * @author cuckoo03
 *
 */
class WikipediaIndex {
	static main(args) {
		Configuration conf = new Configuration()
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs()
		if (otherArgs.length != 2) {
			println "Usage: WikipediaIndex <in> <out>"
			System.exit(2)
		}
		Job job = new Job(conf, "Wikipedia URL Inverted Index")
		job.setJarByClass(WikipediaIndex.class)
		job.setInputFormatClass(TextInputFormat.class)
		job.setMapperClass(WikipediaExtractor.class)
		job.setCombinerClass(Concatenator.class)
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]))
		
		job.setOutputFormatClass(TextOutputFormat.class)
		job.setOutputKeyClass(Text.class)
		job.setOutputValueClass(Text.class)
		job.setReducerClass(Concatenator.class)
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]))
		
		System.exit(job.waitForCompletion(true) ? 0 : 1)
	}
}