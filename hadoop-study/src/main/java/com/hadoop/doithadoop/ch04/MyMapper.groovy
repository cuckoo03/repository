package com.hadoop.doithadoop.ch04

import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Mapper.Context

class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable>{
	private final static LongWritable one = new LongWritable(1);
	private Text word = new Text();
	
	@Override
	public void map(LongWritable key, Text value, Context context) {
		
	}
	
}
