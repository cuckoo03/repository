package com.redis.redisbook.ch07

import java.text.SimpleDateFormat

import redis.clients.jedis.Jedis

class LogReceiverV2 {
	private static final helper = JedisHelper.getInstance()
	private static final String KEY_WAS_LOG = "was:log:list"
	private static final String LOG_FILE_NAME_PREFIX = "./waslog"
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH'.log'")
	
	public void start() {
	}
}
