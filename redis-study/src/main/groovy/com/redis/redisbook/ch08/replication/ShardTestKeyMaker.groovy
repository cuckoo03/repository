package com.redis.redisbook.ch08.replication

import com.redis.redisbook.ch07.redislogger.KeyMaker;

class ShardTestKeyMaker implements KeyMaker {
	private static final String keyPrefix = "Sharding Test-"
	private int index

	public ShardTestKey(int index) {
		this.index = index
	}

	public String getKey() {
		return keyPrefix + index
	}
}
