package com.hadoop.mapreducepatterns.groovy.ch02.medianstd

import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.Reducer.Context

class MedianStdDevReducer extends Reducer<IntWritable, IntWritable, IntWritable,
MedianStdDevTuple> {
	private MedianStdDevTuple result = new MedianStdDevTuple()
	private List<Float> commentLengths = new ArrayList<>()

	@Override
	public void reduce(IntWritable key, Iterable<IntWritable> values,
			Context context) {
		float sum = 0
		float count = 0
		commentLengths.clear()
		result.stddev = 0

		values.each { val ->
			commentLengths.add((float) val.get())
			sum += val.get()
			++count
		}

		// 중앙값을 계산하기 위해 commentLengths를 정렬
		Collections.sort(commentLengths)

		// commentLengths가 짝수 값이면 중앙의 두 값의 평균을 계산
		if ((count % 2) == 0) {
			result.median = (commentLengths.get((int) count /2 - 1)
					+ commentLengths.get((int) count /2)) / 2.0f
		} else {
		// 홀수 값이면 가운데 값을 중앙값으로 설정
			result.median = commentLengths.get((int) count / 2)
		}

		// 표준편차를 계산
		float mean = sum / count
		float sumOfSquares = 0.0f
		commentLengths.each { f ->
			sumOfSquares += (f - mean) * (f - mean)
		}

		result.stddev = (float) Math.sqrt(sumOfSquares / (count - 1))
		context.write(key, result)
	}
}
