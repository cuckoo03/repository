package com.kafka.ch05.producer;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class SimplePartitioner implements Partitioner<String> {
	public SimplePartitioner(VerifiableProperties props) {
	}

	@Override
	public int partition(String key, int numPartitions) {
		int partition = 0;
		int iKey = Integer.parseInt(key);
		if (iKey > 0) {
			partition = iKey % numPartitions;
		}
		return partition;
	}
}