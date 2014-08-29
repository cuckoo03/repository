package com.redis.redisbook.ch07

import junit.framework.Assert

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class LogWriterTest {
	static JedisHelper helper
	static LogWriter logger

	@BeforeClass
	public static void setUpBeforeClass() {
		helper = JedisHelper.getInstance()
		logger = new LogWriter(helper)
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		helper.destroyPool()
	}
	
	@Test
	public void testLogger() {
		Random random = new Random(System.currentTimeMillis())
		for (int i = 0; i < 100; i++) {
			logger.log("logmessage")
			println "log" + i
		}
		
		sleep(random.nextInt(5))
	}
}
