package com.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InvalidJobConfException;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class ComplexWordCountOutputFormat extends
		OutputFormat<Text, LongWritable> {

	/**
	 * 출력 대상에 대한 검증 작업을 수행, 디렉토리에 출력할 경우 이미 해당 디렉토리가 존재하는지 여부를 확인, 이 메서드는
	 * 잡드라이버에 의해 작업이 submit되기 전에 수행된다.
	 */
	@Override
	public void checkOutputSpecs(JobContext context) throws IOException,
			InterruptedException {
		String outputPathName = context.getConfiguration().get(
				"mapred.output.dir");
		if (outputPathName == null) {
			throw new InvalidJobConfException("output directory not set");
		}

		Path outputDir = new Path(outputPathName);
		if (outputDir.getFileSystem(context.getConfiguration()).exists(
				outputDir)) {
			throw new InvalidJobConfException("output directory "
					+ outputPathName + " already exists");
		}
	}

	/**
	 * outputcommiter 객체는 작업의 각 단계가 종료됐을 때 작업을 수행하는 객체다.
	 */
	@Override
	public OutputCommitter getOutputCommitter(TaskAttemptContext arg0)
			throws IOException, InterruptedException {
		return new ComplexWordCountOutputCommitter();
	}

	/**
	 * 리듀스에서 출력하는 각 레코드를 실제 출력 대상에 저장하는 기능을 수행하는 RecordWriter 객체를 반환한다.
	 */
	@Override
	public RecordWriter<Text, LongWritable> getRecordWriter(
			TaskAttemptContext context) throws IOException,
			InterruptedException {
		return new ComplexWordCountRecordWriter(getOutputPath(context),
				context);
	}

	private Path getOutputPath(TaskAttemptContext context) {
		String name = context.getConfiguration().get("mapred.output.dir");
		Path outputPath = (name == null ? null : new Path(name));
		return outputPath;
	}
}