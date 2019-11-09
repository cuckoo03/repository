package com.hadoop.mapreducepatterns.groovy.ch02.medianstd

import groovy.transform.TypeChecked;

import org.apache.hadoop.io.Writable

@TypeChecked
class MedianStdDevTuple implements Writable {
	float median = 0
	float stddev = 0f
	@Override
	public void readFields(DataInput input) throws IOException {
		median = input.readFloat()
		stddev = input.readFloat()
	}
	@Override
	public void write(DataOutput output) throws IOException {
		output.writeFloat(median)
		output.writeFloat(stddev)
	}
	@Override
	public String toString() {
		return median + "\t" + stddev
	}
}