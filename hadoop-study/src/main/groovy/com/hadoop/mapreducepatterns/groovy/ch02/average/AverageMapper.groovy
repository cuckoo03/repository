package com.hadoop.mapreducepatterns.groovy.ch02.average

import java.text.ParseException
import java.text.SimpleDateFormat

import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Mapper.Context

import com.hadoop.mapreducepatterns.MRDPUtils

class AverageMapper extends Mapper<LongWritable, Text, IntWritable, CountAverageTuple> {
	private IntWritable outHour = new IntWritable()
	private CountAverageTuple outCountAverage = new CountAverageTuple()
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

	@Override
	public void map(LongWritable key, Text value, Context context) {
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString())
		def strDate = parsed.get("CreationDate")
		def text = parsed.get("Text")
		if (strDate == null || text == null) {
			// skip this record
			return;
		}

		try {
			Date creationDate= sdf.parse(strDate)
			outHour.set(creationDate.getHours())

			outCountAverage.count = 1
			outCountAverage.average = text.length()

			context.write(outHour, outCountAverage)
		} catch (ParseException e) {
			println e.getMessage()
			return
		}
	}
}