package com.redis.redisbook.ch07

import java.text.SimpleDateFormat

import redis.clients.jedis.Jedis

class LogReceiverV2 {
	private static final helper = JedisHelper.getInstance()
	private static final String KEY_WAS_LOG = "was:log:list"
	private static final String LOG_FILE_NAME_PREFIX = "./waslog"
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH'.log'")

	public void start() {
		Jedis jedis = helper.getConnection()

		while (true) {
			String log = jedis.rpop(KEY_WAS_LOG)
			if (log == null) 
				break
			writeFile(log)
		}
	}

	private void writeFile(String log) {
		if (log == null) {
			return;
		}
		FileWriter writer = new FileWriter(getCurrentFileName(), true)
		println log + ": receives"
		writer.write(log)
		writer.close()
	}

	private String getCurrentFileName() {
		return LOG_FILE_NAME_PREFIX + sdf.format(new Date())
	}
}
