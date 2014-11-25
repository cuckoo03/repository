package com.hadoop.mapreduce;

import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ComplexWordCountMapper extends
		Mapper<LongWritable, Text, Text, LongWritable> {
	private static final LongWritable one = new LongWritable(1);

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		StringTokenizer tokenizer = new StringTokenizer(value.toString());
		while (tokenizer.hasMoreTokens()) {
			context.write(new Text(tokenizer.nextToken()), one);
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(new Date(1284737325887L));
	}
}