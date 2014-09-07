package com.redis.redisbook.ch08.replication

import redis.clients.jedis.Jedis

class DataWriterV2 {
	private Jedis jedis
	
	public DataWriterV2(Jedis jedis) {
		this.jedis = jedis
	}
	
	public Long set(String key, String value) {
		return jedis.lpush(key, value)
	}
}
