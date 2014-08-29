package com.redis.redisbook.ch05

import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.commons.pool.impl.GenericObjectPool.Config

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
/**
 * 커넥션 풀 테스트
 * @author cuckoo03
 *
 */
class JedisPoolTest {
	static main(args) {
		final String IP = "192.168.1.105"
		final int PORT = 6379
		Config config = new Config()
		config.maxActive = 20
		config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK
		
		JedisPool pool = new JedisPool(config, IP, PORT)
		Jedis client1 = pool.getResource()
		client1.hset("info", "name", "name1")
		client1.hset("info", "birth", "birth1")
		
		Jedis client2 = pool.getResource()
		Map<String, String> result = client2.hgetAll("info")
		println "name:" + result.get("name")
		println "birth:" + result.get("birth")
		
		pool.returnResource(client1)
		pool.returnResource(client2)
		pool.destroy()
	}

}
