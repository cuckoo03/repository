package com.hadoop.mapreducepatterns.groovy.ch02.minmaxcount;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

class MinMaxCountTuple implements Writable {
	private Date min = new Date();
	private Date max = new Date();
	private long count = 0;

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
	"yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Override
	public void readFields(DataInput input) throws IOException {
		min = new Date(WritableUtils.readVLong(input));
		max = new Date(WritableUtils.readVLong(input));
		count = WritableUtils.readVLong(input);
	}

	/**
	 * write 메서드에서 WritableUtils를 사용하고 readFields에서 기본형태로 값을 읽을 경우
	 * java.io.EOFException 발생
	 * write 메서드에서 WritableUtils를 사용할 경우에는 readFields에서도 WritableUtils를
	 * 사용해야 에러가 발생하지 않는다.
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		WritableUtils.writeVLong(out, min.getTime());
		WritableUtils.writeVLong(out, max.getTime());
		WritableUtils.writeVLong(out, count);
	}

	@Override
	public String toString() {
		return sdf.format(min) + "\t" + sdf.format(max) + "\t" + count;
	}

	public Date getMin() {
		return min;
	}

	public void setMin(Date min) {
		this.min = min;
	}

	public Date getMax() {
		return max;
	}

	public void setMax(Date max) {
		this.max = max;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}