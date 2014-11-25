package com.hadoop.mapreduce;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.TaskID;

/**
 * RecordWriter는 리듀스에서 출력되는 각 레코드를 작업의 출력 저장소에 저장하는 기능을 수행 
 * @author cuckoo03
 *
 */
public class ComplexWordCountRecordWriter extends
		RecordWriter<Text, LongWritable> {
	private OutputStream out;
	private byte[] keyValueSeperator;

	public ComplexWordCountRecordWriter(Path outputPath,
			TaskAttemptContext context) throws IOException {
		keyValueSeperator = "\t".getBytes("UTF-8");

		TaskID taskId = context.getTaskAttemptID().getTaskID();

		Path taskOutputFile = new Path(outputPath, "wordcount-result-"
				+ taskId.getId());

		FileSystem fs = taskOutputFile
				.getFileSystem(context.getConfiguration());
		out = fs.create(taskOutputFile);
	}

	/**
	 * 리듀스 작업이 종료될때 호출
	 */
	@Override
	public void close(TaskAttemptContext arg0) throws IOException,
			InterruptedException {
		if (out != null) {
			out.close();
		}
	}

	/**
	 * 레코드를 기록
	 */
	@Override
	public void write(Text key, LongWritable value) throws IOException,
			InterruptedException {
		out.write(key.getBytes(), 0, key.getLength());
		out.write(keyValueSeperator);
		out.write(value.toString().getBytes());
		out.write("\n".getBytes());
	}
}