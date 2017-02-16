package com.redis.redisbook.ch05

import org.junit.Test

import redis.clients.jedis.Jedis

class DeleteTest {
	@Test
	public void deleteTest() {
		Jedis jedis = new Jedis("210.122.11.210", 6379)
		jedis.auth("tapaman")
		jedis.flushDB()
		jedis.disconnect()
	}
}
