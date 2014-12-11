package com.hadoop.mapreducepatterns.groovy.ch03.floomfilter

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.util.GenericOptionsParser

/**
 * 사용자 코멘트 목록에 대해 특정 키워드를 갖고 있지 않은 코멘트를 걸러낸다
 * @author cuckoo03
 *
 */
class BloomFiltering {
	static main(args) {
		Configuration conf = new Configuration()
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs()
		if (otherArgs.length != 3) {
			println "Usage:BloomFiltering <in> <cachefile> <out>"
			System.exit(2)
		}
		FileSystem.get(conf).delete(new Path(otherArgs[2]), true)

		Job job = new Job(conf, "Stackoverflow Bloom Filtering")
		job.setJarByClass(BloomFiltering.class)
		job.setMapperClass(BloomFilteringMapper.class)
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]))

		job.setOutputKeyClass(Text.class)
		job.setOutputValueClass(NullWritable.class)
		job.setNumReduceTasks(0)
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[2]))

		DistributedCache.addCacheFile(FileSystem.get(conf).makeQualified(
				new Path(otherArgs[1])).toUri(), conf)
		println "************************"
		println otherArgs[1]
		println "************************"
		// suedo distributed mode 
		//		DistributedCache.addCacheFile(new URI(otherArgs[1]), conf)

		System.exit(job.waitForCompletion(true) ? 0 : 1)
	}
}