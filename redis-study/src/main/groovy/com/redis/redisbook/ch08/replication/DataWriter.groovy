package com.redis.redisbook.ch08.replication

import redis.clients.jedis.Jedis

/**
 * 마스터 노드 데이터 생성 클래스
 * @author cuckoo03
 *
 */
class DataWriter {
	private Jedis jedis
	
	public DataWriter(Jedis jedis) {
		this.jedis = jedis
	}
	public void set(String key, String value) {
		this.jedis.set(key, value)
	}
}
