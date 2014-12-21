package com.hadoop.mapreducepatterns.ch07.random;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class RandomStackOverflowInputFormat extends
		InputFormat<Text, NullWritable> {
	public static final String NUM_MAP_TASKS = "random.generator.map.tasks";
	public static final String NUM_RECORDS_PER_TASK = "random.generator.num.records.per.map.task";
	public static final String RANDOM_WORD_LIST = "random.generator.random.word.file";

	@Override
	public RecordReader<Text, NullWritable> createRecordReader(
			InputSplit split, TaskAttemptContext context) throws IOException,
			InterruptedException {
		RandomStackOverflowRecordReader recordReader = new RandomStackOverflowRecordReader();
		recordReader.initialize(split, context);
		return recordReader;
	}

	@Override
	public List<InputSplit> getSplits(JobContext job) throws IOException,
			InterruptedException {
		// 설정된 맵 태스크 수를 가져온다
		int numSplits = job.getConfiguration().getInt(NUM_MAP_TASKS, -1);
		if (numSplits <= 0) {
			throw new IOException(NUM_MAP_TASKS + " is not set.");
		}
		
		// 태스크 수와 동일한 수의 입력 스플릿을 생성한다
		List<InputSplit> splits = new ArrayList<>();
		for (int i = 0; i < numSplits; i++) {
			splits.add(new FakeInputSplit());
		}
		return splits;
	}

	public static void setNumMapTasks(Job job, int i) {
		job.getConfiguration().setInt(NUM_MAP_TASKS, i);
	}

	public static void setNumRecordPerTasks(Job job, int i) {
		job.getConfiguration().setInt(NUM_RECORDS_PER_TASK, i);
	}

	public static void setRandomWordList(Job job, Path file) {
		DistributedCache.addCacheFile(file.toUri(), job.getConfiguration());
	}
}