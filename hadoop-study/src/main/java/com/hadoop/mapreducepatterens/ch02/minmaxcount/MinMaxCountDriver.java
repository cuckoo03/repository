package com.hadoop.mapreducepatterens.ch02.minmaxcount;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import com.hadoop.mapreducepatterens.MRDPUtils;

public class MinMaxCountDriver {
	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: MinMaxCountDriver <in> <out>");
			System.exit(2);
		}

		Job job = new Job(conf, "MinMaxCount");

		job.setJarByClass(MinMaxCountDriver.class);
		job.setMapperClass(MinMaxCountMapper.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setCombinerClass(MinMaxCountReduce.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));

		job.setReducerClass(MinMaxCountReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MinMaxCountTuple.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		job.waitForCompletion(true);
	}
}

class MinMaxCountMapper extends Mapper<Object, Text, Text, MinMaxCountTuple> {
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

class MinMaxCountReduce extends
		Reducer<Text, MinMaxCountTuple, Text, MinMaxCountTuple> {
	private MinMaxCountTuple result = new MinMaxCountTuple();

	@Override
	public void reduce(Text key, Iterable<MinMaxCountTuple> values,
			Context context) throws IOException, InterruptedException {
		result.setMin(null);
		result.setMax(null);
		result.setCount(0);
		int sum = 0;

		for (MinMaxCountTuple val : values) {
			// 입력의 최소값이 결과의 최소값보다 작으면 입력의 최소값을 결과의 최소값으로 설정
			if (result.getMin() == null
					|| val.getMin().compareTo(result.getMin()) < 0) {
				result.setMin(val.getMin());
			}

			// 입력의 최대값이 결과의 최대값보다 크다면 입력의 최대값을 결과의 최대값으로 설정
			if (result.getMax() == null
					|| val.getMax().compareTo(result.getMax()) > 0) {
				result.setMax(val.getMax());
			}

			sum += val.getCount();

			result.setCount(sum);
			context.write(key, result);
		}
	}
}

class MinMaxCountTuple implements Writable {
	private Date min = new Date();
	private Date max = new Date();
	private long count = 0;

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Override
	public void readFields(DataInput in) throws IOException {
		min = new Date(in.readLong());
		max = new Date(in.readLong());
		count = in.readLong();
	}

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
