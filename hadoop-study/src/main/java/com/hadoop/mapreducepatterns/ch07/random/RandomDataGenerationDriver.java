package com.hadoop.mapreducepatterns.ch07.random;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 랜덤한 스택오버플로우 데이터를 생성하기 위해 단어 1000개짜리 목록을 이용하여 랜덤한 코멘트를 만든다. 또한 랜덤 점수, 랜덤 로우ID,
 * 랜덤사용자ID, 랜덤 생성일을 만들어야 한다.
 * 
 * @author cuckoo03
 *
 */
public class RandomDataGenerationDriver {
	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 4) {
			System.err
					.println("Usage: RandomDataGeneration <numMapTasks> <numRecordsPerTask> <wordList> <out>");
			System.exit(2);
		}
		int numMapTasks = Integer.parseInt(otherArgs[0]);
		int numRecordsPerTask = Integer.parseInt(otherArgs[1]);
		Path wordList = new Path(otherArgs[2]);
		Path outputDir = new Path(args[3]);

		Job job = new Job(conf, "RandomDataGenerationDriver");
		job.setJarByClass(RandomDataGenerationDriver.class);

		job.setNumReduceTasks(0);

		job.setInputFormatClass(RandomStackOverflowInputFormat.class);

		RandomStackOverflowInputFormat.setNumMapTasks(job, numMapTasks);
		RandomStackOverflowInputFormat.setNumRecordPerTasks(job,
				numRecordsPerTask);
		RandomStackOverflowInputFormat.setRandomWordList(job, wordList);

		TextOutputFormat.setOutputPath(job, outputDir);

		FileSystem.get(conf).delete(outputDir, true);
		System.out.println("outdir delete.");
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		/*
		FileStatus[] files = FileSystem.get(conf).listStatus(wordList);
		for (FileStatus status : files) {
			DistributedCache.addCacheFile(status.getPath().toUri(), conf);
		}
		System.out.println("flies length:" + files.length);
		*/

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}