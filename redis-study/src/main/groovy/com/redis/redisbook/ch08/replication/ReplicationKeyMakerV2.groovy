package com.redis.redisbook.ch08.replication

import com.redis.redisbook.ch07.redislogger.KeyMaker

class ReplicationKeyMakerV2 implements KeyMaker {
	private static final String keyPrefix = "Replication List-"
	private int index
	public ReplicationKeyMakerV2(int index) {
		this.index = index
	}
	public String getKey() {
		return keyPrefix + index;
	}

}
