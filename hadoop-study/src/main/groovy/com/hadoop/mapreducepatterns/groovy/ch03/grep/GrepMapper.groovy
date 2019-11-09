package com.hadoop.mapreducepatterns.groovy.ch03.grep

import groovy.transform.TypeChecked;

import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Mapper.Context

@TypeChecked
class GrepMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
	@Override
	public void map(LongWritable key, Text value, Context context) {
		String txt = value.toString()
		String mapRegex = context.getConfiguration().get("mapregex")
		
		if (txt.matches(mapRegex)) {
			context.write(NullWritable.get(), value)
		}
	}
}