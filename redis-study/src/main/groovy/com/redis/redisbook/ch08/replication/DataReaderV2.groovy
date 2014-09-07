package com.redis.redisbook.ch08.replication

import redis.clients.jedis.Jedis

class DataReaderV2 {
	private Jedis jedis
	private static final int TIMEOUT = 1000
	
	public DataReaderV2(Jedis jedis) {
		this.jedis = jedis
	}
	
	public List<String> get(String key) {
		return jedis.brpop(TIMEOUT, key)
	}
}
