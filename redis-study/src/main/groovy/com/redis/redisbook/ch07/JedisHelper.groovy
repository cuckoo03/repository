package com.redis.redisbook.ch07

import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.commons.pool.impl.GenericObjectPool.Config

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool

class JedisHelper {
	protected static final String HOST = "192.168.1.101"
	protected static final int PORT = 6300
	protected static final Set<Jedis> connectionList = new HashSet<Jedis>()
	private JedisPool pool

	private JedisHelper() {
		Config config = new Config()
		config.maxActive = 20
		config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK

		this.pool = new JedisPool(config, HOST, PORT, 5000, "12341234")
	}

	private static class LazyHolder {
		private static final JedisHelper INSTANCE = new JedisHelper()
	}

	public static JedisHelper getInstance() {
		return LazyHolder.INSTANCE
	}

	public final Jedis getConnection() {
		Jedis jedis = this.pool.getResource()
		this.connectionList.add(jedis)

		return jedis
	}

	public final void returnResource(Jedis jedis) {
		this.pool.returnResource(jedis)
	}

	public final void destroyPool() {
		Iterator<Jedis> iter = this.connectionList.iterator()
		while (iter.hasNext()) {
			this.pool.returnResource(iter.next())
		}

		this.pool.destroy()
	}
}
