package com.hadoop.mapreducepatterns.groovy.ch03.random

import groovy.transform.TypeChecked;

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Mapper.Context
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.util.GenericOptionsParser

/**
 * SRS를 통해 뽁힐 확률이 같은 레코드로 구성된 데이터 셋의 서브셋을 출력한다.
 * @author cuckoo03
 *
 */
@TypeChecked
class SimpleRandomSampling {
	public static class SRSMapper extends Mapper<LongWritable, Text,
	NullWritable, Text> {
		private Random random = new Random()
		private Double percentage
		@Override
		protected void setup(Context context) {
			String strPercentage = context.getConfiguration().
					get("filter_percentage")
			percentage = Double.parseDouble(strPercentage) / 100.0
		}
		@Override
		public void map(LongWritable key, Text value, Context context) {
			if (random.nextDouble() < percentage) {
				context.write(NullWritable.get(), value)
			}
		}
	}
	static main(String[] args) {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 3) {
			System.err.println("Usage: SRS <percentage> <in> <out>");
			System.exit(2);
		}
		conf.set("filter_percentage", otherArgs[0]);
		Job job = new Job(conf, "SRS");
		job.setJarByClass(SimpleRandomSampling.class);
		job.setMapperClass(SRSMapper.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[1]));

		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		job.setNumReduceTasks(0); // Set number of reducers to zero
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[2]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
