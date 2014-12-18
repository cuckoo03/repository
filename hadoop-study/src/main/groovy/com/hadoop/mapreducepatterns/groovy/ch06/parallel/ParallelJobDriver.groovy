package com.hadoop.mapreducepatterns.groovy.ch06.parallel

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.DoubleWritable
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.Mapper.Context
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser

import com.hadoop.mapreducepatterns.groovy.ch06.parallel.ParallelJobDriver.AverageReputationReducer;

/**
 * 앞 예제의 각 빈으로 분류된 사용자 출력이 주어질 때 양쪽 빈에 대해 잡을 병렬로 실행하여 각 사용자의 평판 평균을 계산한다
 * 
 * @author cuckoo03
 *
 */
class ParallelJobDriver {
	public static class AverageReputationMapper extends Mapper<LongWritable,
	Text, Text, DoubleWritable> {
		private static final Text GROUP_ALL_KEY = new Text("Average Reputation")
		private DoubleWritable outvalue = new DoubleWritable()

		@Override
		public void map(LongWritable key, Text value, Context context) {
			// TextOutputFormat으로 출력된 레코드를 분할한다
			String[] tokens = value.toString().split("\t")

			double reputation = Double.parseDouble(tokens[2])

			outvalue.set(reputation)
			context.write(GROUP_ALL_KEY, outvalue)
		}
	}
	public static class AverageReputationReducer extends Reducer<Text,
	DoubleWritable, Text, DoubleWritable> {
		private DoubleWritable outvalue = new DoubleWritable()
		@Override
		public void reduce(Text key, Iterable<DoubleWritable> values,
				org.apache.hadoop.mapreduce.Reducer.Context context) {
			double sum = 0.0
			double count = 0
			values.each { dw ->
				sum += dw.get()
				++count
			}

			outvalue.set(sum / count)
			context.write(key, outvalue)
		}
	}
	static main(args) {
		Configuration conf = new Configuration()
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs()
		if (otherArgs.length != 4) {
			println "Usage:ParallelJob <belowInputDir> <aboveInputDir> <belowOutputDir> <aboveOutputDir>"
			System.exit(2)
		}

		Path belowAvgInputDir = new Path(otherArgs[0])
		Path aboveAvgInputDir = new Path(otherArgs[1])
		Path belowAvgOutputDir = new Path(otherArgs[2])
		Path aboveAvgOutputDir = new Path(otherArgs[3])

		Job belowAvgJob = submitJob(conf, belowAvgInputDir, belowAvgOutputDir)
		Job aboveAvgJob = submitJob(conf, aboveAvgInputDir, aboveAvgOutputDir)

		// 두 잡이 끝나지 않는 동안 슬립
		while (!belowAvgJob.isComplete() || !aboveAvgJob.isComplete()) {
			Thread.sleep(5000)
		}

		if (belowAvgJob.isSuccessful()) {
			println "Below average job completed successfully"
		} else {
			println "Below average job failed"
		}

		if (aboveAvgJob.isSuccessful()) {
			println "Above average job completed successfully"
		} else {
			println "Above average job failed"
		}

		System.exit(belowAvgJob.isSuccessful() && aboveAvgJob.isSuccessful() ? 0 : 1)
	}

	private static Job submitJob(Configuration conf, Path inputDir,
			Path outputDir) {
		Job job = new Job(conf, "ParallelJobs")
		job.setJarByClass(ParallelJobDriver.class)

		job.setMapperClass(AverageReputationMapper.class)
		job.setReducerClass(AverageReputationReducer.class)

		job.setOutputKeyClass(Text.class)
		job.setOutputValueClass(DoubleWritable.class)

		job.setInputFormatClass(TextInputFormat.class)
		TextInputFormat.addInputPath(job, inputDir)

		job.setOutputFormatClass(TextOutputFormat.class)
		TextOutputFormat.setOutputPath(job, outputDir)

		job.submit()
		return job
	}
}
