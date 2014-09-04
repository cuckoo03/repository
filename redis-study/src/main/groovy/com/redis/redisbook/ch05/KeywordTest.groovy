package com.redis.redisbook.ch05

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import redis.clients.jedis.Jedis

import com.redis.redisbook.ch07.JedisHelper

class KeywordTest {
	static JedisHelper helper
	static Jedis jedis
	
	@BeforeClass
	public static void setUp() {
		helper = JedisHelper.getInstance()
		jedis = helper.getConnection()
	}
	
	@AfterClass
	public static void tearDown() {
		helper.destroyPool()
	}
	
	@Test
	public void deleteKey() {
		println jedis.flushDB()
	}
}
