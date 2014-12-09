package com.hadoop.mapreducepatterns.groovy.ch02.reversedIndex

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.Reducer.Context

class Concatenator extends Reducer <Text, Text, Text, Text>{
	private Text result = new Text()
	@Override
	public void reduce(Text key , Iterable<Text> values, Context context) {
		StringBuilder sb = new StringBuilder()
		boolean first = true
		values.each { id ->
			sb.append(id.toString() + " ")
		}

		result.set(sb.toString())
		context.write(key, result)
	}
}
