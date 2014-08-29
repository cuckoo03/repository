package com.redis.redisbook.ch05

import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.commons.pool.impl.GenericObjectPool.Config

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool



/**
 * 천만 건의 데이터를 다중 스레드로 전송하는 예제
 * thread count 5
 * 초당 처리 건수 31.1332503113325031000.0
 * 시간 3.212 sec
 * @author cuckoo03
 *
 */
class JedisThreadTest {
	private static final float TOTAL_OP = 100000f
	private static final int THREAD = 5

	static main(args) {
		final String IP = "192.168.1.105"
		final int PORT = 6379

		Config config = new Config()
		config.maxActive = 500
		config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK
		final JedisPool pool = new JedisPool(config, IP, PORT, 10000)

		final long start = now()
		Runtime.addShutdownHook {
			long elapsed = now() - start
			println "thread count " + THREAD
			println "초당 처리 건수 " + TOTAL_OP / elapsed + 1000f
			println "소요 시간 " + elapsed / 1000f + " sec"
		}

		JedisThreadTest test = new JedisThreadTest()
		int cnt = 0
		THREAD.times {
			test.makeWorker(pool, cnt).start()
			cnt++
		}
	}

	private Thread makeWorker(final JedisPool pool, final int idx) {
		Thread t = new Thread(){
					@Override
					public void run() {
						String key, value
						Jedis jedis = pool.getResource()

						int i = 1
						10000.times {
							if (i % 5 == idx) {
								key = value = String.valueOf("key" + (10000 + i))
								jedis.set(key, value)
							}
							i++
						}

						pool.returnResource(jedis)
					}
				}

		return t
	}

	private static long now() {
		return System.currentTimeMillis()
	}
}
