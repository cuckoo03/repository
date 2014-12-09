package com.hadoop.mapreducepatterens.ch02.minmaxcount;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.hadoop.mapreducepatterens.MRDPUtils;

public class MinMaxCountMapper extends Mapper<Object, Text, Text, MinMaxCountTuple> {
	private Text outUserId = new Text();
	private MinMaxCountTuple outTuple = new MinMaxCountTuple();

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Override
	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(value
				.toString());

		try {
			String strDate = parsed.get("CreationDate");
			String userId = parsed.get("UserId");
			Date creationDate = sdf.parse(strDate);
			outTuple.setMin(creationDate);
			outTuple.setMax(creationDate);

			outTuple.setCount(1);
			outUserId.set(userId);

			context.write(outUserId, outTuple);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
