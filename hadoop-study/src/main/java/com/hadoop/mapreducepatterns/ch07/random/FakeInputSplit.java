package com.hadoop.mapreducepatterns.ch07.random;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

/**
 * 입력 스플릿은 램덤 데이터를 생성하는 태스크를 부여하여 프레임워크를 속이는데 사용된다.
 * @author cuckoo03
 *
 */
public class FakeInputSplit extends InputSplit implements Writable {

	@Override
	public long getLength() throws IOException, InterruptedException {
		return 0;
	}

	@Override
	public String[] getLocations() throws IOException, InterruptedException {
		return new String[0];
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
	}
}