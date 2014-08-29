package com.redis.redisbook.ch07

import java.text.SimpleDateFormat

import redis.clients.jedis.Jedis

class LogWriterV2 {
	private static final String KEY_WAS_LOG = "was:log:list"
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss SSS")
	private JedisHelper helper
	
	public LogWriterV2(JedisHelper helper) {
		this.helper = helper
	}
	
	public Long log(String log) {
		Jedis jedis = this.helper.getConnection()
		Long rtn = jedis.lpush(KEY_WAS_LOG, 
			sdf.format(new Date()) + log + "\n")
		
		this.helper.returnResource(jedis)
		
		return rtn
	}

}
