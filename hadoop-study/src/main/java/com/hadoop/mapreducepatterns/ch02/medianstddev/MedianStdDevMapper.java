package com.hadoop.mapreducepatterns.ch02.medianstddev;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.hadoop.mapreducepatterens.MRDPUtils;

class MedianStdDevMapper extends
		Mapper<LongWritable, Text, IntWritable, IntWritable> {
	private IntWritable outHour = new IntWritable();
	private IntWritable outCommentLength = new IntWritable();
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(value
				.toString());

		String strDate = parsed.get("CreationDate");
		String text = parsed.get("Text");
		Date creationDate;
		try {
			creationDate = sdf.parse(strDate);
			outHour.set(creationDate.getHours());

			outCommentLength.set(text.length());

			context.write(outHour, outCommentLength);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}