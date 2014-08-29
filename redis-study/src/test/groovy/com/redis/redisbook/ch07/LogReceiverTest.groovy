package com.redis.redisbook.ch07

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class LogReceiverTest {
	static JedisHelper helper

	@BeforeClass
	public static void setUp() {
		helper = JedisHelper.getInstance()
	}

	@AfterClass
	public static void tearDown() {
		helper.destroyPool()
	}

	@Test
	public void testLogger() {
		LogReceiver receiver = new LogReceiver()
		receiver.start()
	}
}
