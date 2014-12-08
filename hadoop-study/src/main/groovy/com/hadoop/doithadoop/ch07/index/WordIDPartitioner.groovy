package com.hadoop.doithadoop.ch07.index

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Partitioner

import com.hadoop.doithadoop.ch07.index.WordID;

class WordIDPartitioner extends Partitioner<WordID, Text>{
	protected WordIDPartitioner() {
	}
	/**
	 * WordID에서 단어맨 빼내 (getWord()) 그것의 해시값을 구한 다음에 
	 * 파티션 숫자로 나머지를 구해서 리턴한다.
	 */
	@Override
	public int getPartition(WordID key, Text value, int numPartitions) {
		return (key.word.hashCode() & Integer.MAX_VALUE) % numPartitions
	}
}