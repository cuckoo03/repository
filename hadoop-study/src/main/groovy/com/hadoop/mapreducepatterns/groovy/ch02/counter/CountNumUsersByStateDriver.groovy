package com.hadoop.mapreducepatterns.groovy.ch02.counter

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.util.GenericOptionsParser

class CountNumUsersByStateDriver {
	static main(args) {
		Configuration conf = new Configuration()
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs()
		if (otherArgs.length != 2) {
			println "Usage: CountNumUsersByState <in> <out>"
			System.exit(2)
		}

		Job job = new Job(conf, "Count Num Users By State")
		job.setJarByClass(CountNumUsersByStateDriver.class)
		job.setMapperClass(CountNumUsersByStateMapper.class)
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]))

		job.setOutputKeyClass(NullWritable.class)
		job.setOutputValueClass(NullWritable.class)
		job.setNumReduceTasks(0)

		Path outputDir = new Path(otherArgs[1])
		FileOutputFormat.setOutputPath(job, outputDir)

		int code = job.waitForCompletion(true) ? 0 : 1
		println "***************************************"
		println "code:$code"
		println "counter group size:" + job.getCounters().getGroup(CountNumUsersByStateMapper.
				STATE_COUNTER_GROUP).size()
		
		if (code == 0) {
			job.getCounters().getGroup(CountNumUsersByStateMapper.
					STATE_COUNTER_GROUP).each {counter ->
						println counter.getDisplayName() + "\t" +
								counter.getValue()
					}
		}
		println "***************************************"

		FileSystem.get(conf).delete(outputDir, true);
		System.exit(code)
	}
}
