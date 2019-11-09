package com.hadoop.mapreducepatterns.groovy.ch02.average

import groovy.transform.TypeChecked;

import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.Reducer.Context

@TypeChecked
class AverageReducer extends Reducer<IntWritable, CountAverageTuple,
IntWritable , CountAverageTuple>{
	private CountAverageTuple result = new CountAverageTuple()

	@Override
	public void reduce(IntWritable key, Iterable<CountAverageTuple> values,
			Context context) {
		float sum = 0
		float count = 0

		values.each {val->
			sum += val.count * val.average
			count+= val.count
		}

		result.count = count
		result.average = sum / count

		context.write(key, result)
	}
}