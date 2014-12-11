package com.hadoop.mapreducepatterns.groovy.ch02.medianstd

import java.text.SimpleDateFormat

import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Mapper.Context

import com.hadoop.mapreducepatterns.MRDPUtils;

class MedianStdDevMapper extends Mapper<LongWritable, Text, IntWritable,
IntWritable>{
	private IntWritable outHour = new IntWritable()
	private IntWritable outCommentLength = new IntWritable()
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
	@Override
	public void map(LongWritable key, Text value, Context context) {
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString())

		// creationDate 필드 기준으로 그룹핑
		String strDate = parsed.get("CreationDate")
		String text = parsed.get("Text")
		Date creationDate = sdf.parse(strDate)
		outHour.set(creationDate.getHours())

		outCommentLength.set(text.length())
		
		context.write(outHour, outCommentLength)
	}
}