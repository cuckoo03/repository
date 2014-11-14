package com.redis.redisbook.ch08.replication

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import static org.junit.Assert.fail
import redis.clients.jedis.Jedis

import com.redis.redisbook.ch07.redislogger.KeyMaker

/**
 * 복제 확인 테스트
 * 10000개 소요시간:11 sec
 * 100000개 소요시간:106 sec
 * @author cuckoo03
 *
 */
class ReplicationTest {
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
		DataWriter writer = new DataWriter(master)
		DataReader reader = new DataReader(slave)
		Long start = System.currentTimeMillis()
		
		TEST_COUNT.times {
			KeyMaker keyMaker = new ReplicationKeyMaker(it)
			String value = "test value $it"
			writer.set(keyMaker.getKey(), value)
			String result = reader.get(keyMaker.getKey())
			
			if (value.equals(result)) {
				println result
			} else {
				// fail("the value not match result. $value, $result")
			}
		}
		Long end = System.currentTimeMillis()
		println end.minus(start)
	}
}
