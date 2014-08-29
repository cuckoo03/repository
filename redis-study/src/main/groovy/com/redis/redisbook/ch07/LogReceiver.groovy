package com.redis.redisbook.ch07

import java.text.SimpleDateFormat

import redis.clients.jedis.Jedis

/**
 * 레디스 서버에서 로그를 읽어서 파일로 저장
 * @author cuckoo03
 *
 */
class LogReceiver {
	private static final JedisHelper helper = JedisHelper.getInstance()
	private static final String KEY_WAS_LOG = "was:log"
	private static final String LOG_FILE_NAME_PREFIX = "./waslog"
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH'.log'")
	private static final int WAITING_TERM = 50
	
	public void start() {
		Random random = new Random()
		Jedis jedis = helper.getConnection()
		while (true) {
			writeFile(jedis.getSet(KEY_WAS_LOG, ""))
			
			try {
				sleep(random.nextInt(WAITING_TERM))
			} catch (InterruptedException e) {
				println e
			}
		}
	}
	
	public String getCurrentFileName() {
		return LOG_FILE_NAME_PREFIX + sdf.format(new Date())
	}
	
	private writeFile(String log) {
		FileWriter writer = new FileWriter(getCurrentFileName(), true)
		
		writer.write(log)
		writer.flush()
		println "writeFile : " + log
	}
}
