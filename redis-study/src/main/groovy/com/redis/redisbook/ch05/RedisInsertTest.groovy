package com.redis.redisbook.ch05

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool

/**
 * 천만 건의 데이터를 전송하는 예제
 * 
 * 만건 초당 처리 건수 2259.x
 * 소요 시간 4.425
 * @author cuckoo03
 *
 */
class RedisInsertTest {
	private static final float TOTAL_OP = 10000f



	static main(args) {
		final String IP = "192.168.1.105"
		final int PORT = 6379

		JedisPool pool = new JedisPool(IP, PORT)
		Jedis jedis = pool.getResource()
		String key, value
		long start = now()
		long loopTime = now()

		int i = 1
		TOTAL_OP.times {it->
			key = value = String.valueOf("key" + (10000 + it))
			jedis.set(key, value)
		}

		long elapsed = now() - start
		println "초당 처리 건수 " + TOTAL_OP / elapsed * 1000f
		println "소요 시간 " + elapsed / 1000f + "seconds"
		jedis.disconnect()
	}

	private static long now() {
		return System.currentTimeMillis()
	}
}
