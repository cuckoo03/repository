package com.hadoop.mapreduce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * 파일에서 한 라인을 읽어 키에는 현재의 파일 위치 오프셋 정보를 값에는 라인의 텍스트 정보를 읽는 기능을 수행한다.
 * @author cuckoo03
 *
 */
public class ComplexWordCountLineRecordReader extends
		RecordReader<LongWritable, Text> {
	private long start;
	private long pos;
	private long end;
	private BufferedReader reader;
	private LongWritable key = null;
	private Text value = null;

	/**
	 * 데이터를 모두 읽으면 오픈한 자원을 모두 해제
	 */
	@Override
	public void close() throws IOException {
		if (reader != null) {
			reader.close();
		}
	}

	/**
	 * 읽혀진 현재 키 데이터
	 */
	@Override
	public LongWritable getCurrentKey() throws IOException,
			InterruptedException {
		return key;
	}

	/**
	 * 읽혀진 현재 값 데이터
	 */
	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		return value;
	}
	
	/**
	 * 현재 맵 태스크의 진행 상황을 반환한다. 0~1 사이의 값
	 */
	@Override
	public float getProgress() throws IOException, InterruptedException {
		if (start == end) {
			return 0.0f;
		} else {
			return Math.min(1.0f, (pos - start) / (end - start));
		}
	}

	@Override
	public void initialize(InputSplit genericSplit, TaskAttemptContext context)
			throws IOException, InterruptedException {
		FileSplit split = (FileSplit) genericSplit;
		Configuration conf = context.getConfiguration();
		this.start = split.getStart();
		this.end = this.start + split.getLength();
		final Path path = split.getPath();
		FileSystem fs = path.getFileSystem(conf);
		this.reader = new BufferedReader(new InputStreamReader(fs.open(path)));
		this.pos = this.start;
		context.setStatus(path + " (" + start + " ~ " + end + ")");
	}

	/**
	 * 입력 데이터로부터 다음 레코드를 읽는다. 더 이상 데이터가 없으면 false를 반환
	 */
	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (key == null) {
			key = new LongWritable();
		}
		key.set(pos);

		if (value == null) {
			value = new Text();
		}

		String line = reader.readLine();
		if (line == null) {
			key = null;
			return false;
		}
		pos += line.getBytes().length;
		if (pos >= end) {
			key = null;
			value = null;
			return false;
		}
		value.set(line.getBytes("utf-8"));
		
		return true;
	}
}