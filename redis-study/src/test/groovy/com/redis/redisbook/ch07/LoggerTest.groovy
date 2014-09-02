package com.redis.redisbook.ch07

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class LoggerTest {
	static JedisHelper helper
	private static final int WAITING_TERM = 50

	@BeforeClass
	public static void setUp() {
		helper = JedisHelper.getInstance()
	}

	@AfterClass
	public static void tearDown() {
		helper.destroyPool()
	}
	@Test
	public void testWrite() {
		Random random = new Random(System.currentTimeMillis())
		LogWriterV2 logWriter = new LogWriterV2(helper)
		for (int i = 0; i < 100; i++) {
			logWriter.log(i + ", This is new test log message")
			println i + " sends"

			sleep(random.nextInt(50))
		}
	}

	public void testReceive() {
		LogReceiverV2 logReceiver = new LogReceiverV2()

		for (int i = 0; i < 5; i++) {
			logReceiver.start()

			sleep(WAITING_TERM)
		}
	}
}
