package com.redis.redisbook.ch08.replication

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import static org.junit.Assert.*

import redis.clients.jedis.ShardedJedis

/**
 * 해시 기반 샤딩 테스트
 * @author cuckoo03
 *
 */
class ShardingTest {
	private static final int TEST_COUNT = 5
	static ShardedJedisHelper helper

	@BeforeClass
	public static void setUpClass() {
		helper = ShardedJedisHelper.getInstance()
	}

	@AfterClass
	public static void tearDownClass() {
		helper.destroyPool()
	}
	@Test
	public void testSharding() {
		ShardedJedis jedis = helper.getConnection()

		TEST_COUNT.times {
			String testValue = "Test value:" + it
			ShardTestKeyMaker keyMaker = new ShardTestKeyMaker(it)
			jedis.set(keyMaker.getKey(), testValue)
			assertEquals(testValue, jedis.get(keyMaker.getKey()))
		}
	}
}
