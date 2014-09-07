package com.redis.redisbook.ch08.replication

import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.commons.pool.impl.GenericObjectPool.Config

import redis.clients.jedis.JedisShardInfo
import redis.clients.jedis.ShardedJedis
import redis.clients.jedis.ShardedJedisPool
import redis.clients.util.Hashing

class SharededJedisHelper {
	protected static final String SHARD1_HOST = "192.168.1.101"
	protected static final int SHARD1_PORT = 6380
	protected static final String SHARD2_HOST = "192.168.1.101"
	protected static final int SHARD2_PORT = 6381
	
	private final Set<SharededJedis> connectionList = new HashSet<>()
	
	private ShardedJedisPool shardedPool
	
	private static class LazyHolder {
		private static final SharededJedisHelper INSTANCE = new SharededJedisHelper()
	}
	
	private SharededJedisHelper() {
		Config config = new Config()
		config.maxActive = 20
		config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK
		
		List<JedisShardInfo>shards = new ArrayList<>()
		shards.add(new JedisShardInfo(SHARD1_HOST, SHARD1_PORT)) 
		shards.add(new JedisShardInfo(SHARD2_HOST, SHARD2_PORT)) 
		
		shardedPool = new ShardedJedisPool(config, shards, Hashing.MURMUR_HASH)
	}
	
	public static SharededJedisHelper getInstance() {
		return LazyHolder.INSTANCE
	}
	
	public final SharededJedis getConnection() {
		ShardedJedis jedis = shardedPool.getResource()
		connectionList.add(jedis)
		return jedis 
	}
	
	public final void returnResource(SharededJedis jedis) {
		this.shardedPool.returnResource(jedis)
	}
	
	public final void destroyPool() {
		Iterator<ShardedJedis> jedisList = connectionList.iterator()
		while (jedisList.hasNext()) {
			SharededJedis jedis = jedisList.next()
			shardedPool.returnResource(jedis)
		} 
		
		shardedPool.destroy()
	}
}
