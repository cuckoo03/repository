package com.hadoop.mapreducepatterns.groovy.ch02.minmaxcount;

import groovy.transform.TypeChecked;

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.Reducer.Context

@TypeChecked
public class MinMaxCountReduce extends
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
			sum += val.getCount().toInteger();
		}
		result.setCount(sum);
		context.write(key, result);
	}
}
