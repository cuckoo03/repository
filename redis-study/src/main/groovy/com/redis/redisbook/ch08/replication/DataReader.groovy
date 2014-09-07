package com.redis.redisbook.ch08.replication

import redis.clients.jedis.Jedis

class DataReader {
	private Jedis jedis

	public DataReader(Jedis jedis) {
		this.jedis = jedis
	}

	public String get(String key) {
		return this.jedis.get(key)
	}
}
