package com.redis.redisbook.ch07

import java.text.SimpleDateFormat

import redis.clients.jedis.Jedis

/**
 * 레디스에 로그를 기록
 * @author cuckoo03
 *
 */
class LogWriter {
	private static final String KEY_WAS_LOG = "was:log"
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss SSS ")
	JedisHelper helper
	
	LogWriter(JedisHelper helper) {
		this.helper = helper
	}
	
	Long log(String log) {
		Jedis jedis = this.helper.getConnection()
		Long rtn = jedis.append(KEY_WAS_LOG, sdf.format(new Date()) + log + "\n")
		
		this.helper.returnResource(jedis)
		return rtn
	}
}
