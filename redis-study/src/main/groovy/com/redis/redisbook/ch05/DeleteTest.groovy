package com.redis.redisbook.ch05

import org.junit.Test

import redis.clients.jedis.Jedis

class DeleteTest {
	@Test
	public void deleteTest() {
		Jedis jedis = new Jedis("192.168.1.101", 6300)
		jedis.flushDB()
		jedis.disconnect()
	}
}
