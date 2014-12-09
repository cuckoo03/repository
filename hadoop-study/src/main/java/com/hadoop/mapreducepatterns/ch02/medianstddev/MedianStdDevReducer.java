package com.hadoop.mapreducepatterns.ch02.medianstddev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

class MedianStdDevReducer extends
		Reducer<IntWritable, IntWritable, IntWritable, MedianStdDevTuple> {
	private MedianStdDevTuple result = new MedianStdDevTuple();
	private List<Float> commentLengths = new ArrayList<>();

	@Override
	public void reduce(IntWritable key, Iterable<IntWritable> values,
			Context context) throws IOException, InterruptedException {
		float sum = 0;
		float count = 0;
		commentLengths.clear();
		result.stddev = 0;

		for (IntWritable val : values) {
			commentLengths.add((float) val.get());
			sum += val.get();
			++count;
		}

		Collections.sort(commentLengths);

		if ((count % 2) == 0) {
			result.median = (commentLengths.get((int) count / 2 - 1) + commentLengths
					.get((int) count / 2)) / 2.0f;
		} else {
			result.median = commentLengths.get((int) count / 2);
		}

		float mean = sum / count;
		float sumOfSquares = 0.0f;
		for (float f : commentLengths) {
			sumOfSquares += (f - mean) * (f - mean);
		}

		result.stddev = (float) Math.sqrt(sumOfSquares / (count - 1));
		context.write(key, result);
	}
}
