package com.redis.redisbook.ch08.replication

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import static org.junit.Assert.*

import redis.clients.jedis.Jedis

import com.redis.redisbook.ch07.redislogger.KeyMaker
/**
 * 10000개 소요시간:11 sec
 * 100000개 소요시간:106671 sec
 * @author cuckoo03
 *
 */
class ReplicationTestV2 {
	private static final int TEST_COUNT = 100000
	static Jedis master
	static Jedis slave

	@BeforeClass
	public static void setUpClass() {
		master = new Jedis("192.168.1.101", 6300)
		slave = new Jedis("192.168.1.101", 6301)
	}
	@AfterClass
	public static void tearDownClass() {
		master.disconnect()
		slave.disconnect()
	}
	@Test
	public void replicationTest() {
		DataWriterV2 writer = new DataWriterV2(master)
		DataReaderV2 reader = new DataReaderV2(slave)
		long current = 0
		long start = System.currentTimeMillis()
		
		TEST_COUNT.times {
			KeyMaker keyMaker = new ReplicationKeyMakerV2(it)
			String value = "test value$it"
			writer.set(keyMaker.getKey(), value)

			current = System.currentTimeMillis()
			List<String> result = reader.get(keyMaker.getKey())
			long elapsed = System.currentTimeMillis() - current

			if (value.equals(result.get(1))) {
				if (elapsed > 1) {
					println "elapsed:" + elapsed + ", count:$it"
				}
			} else {
				fail("not match result. $value:$result")
			}
		}
		long end = System.currentTimeMillis()
		println end - start
	}
}
