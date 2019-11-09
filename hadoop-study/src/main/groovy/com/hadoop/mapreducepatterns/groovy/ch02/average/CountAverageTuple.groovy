package com.hadoop.mapreducepatterns.groovy.ch02.average

import groovy.transform.TypeChecked;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable
import org.apache.hadoop.io.WritableUtils;

@TypeChecked
class CountAverageTuple implements Writable {
	float count = 0f
	float average = 0f

	@Override
	public void readFields(DataInput input) throws IOException {
		count = input.readFloat()
		average = input.readFloat()
	}
	@Override
	public void write(DataOutput output) throws IOException {
		output.writeFloat(count)
		output.writeFloat(average)
	}
	@Override
	public String toString() {
		return count + "\t" + average
	}
}