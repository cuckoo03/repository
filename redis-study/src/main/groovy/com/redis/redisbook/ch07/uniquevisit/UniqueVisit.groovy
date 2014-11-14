package com.redis.redisbook.ch07.uniquevisit

import java.text.SimpleDateFormat

import redis.clients.jedis.Jedis
import redis.clients.jedis.Pipeline

import com.redis.redisbook.ch07.JedisHelper

/**
 * 순 방문 횟수 예제
 * @author cuckoo03
 *
 */
class UniqueVisit {
	private Jedis jedis
	private static final String KEY_PAGE_VIEW = "page:view:"
	private static final String KEY_UNIQUE_VISITOR = "qunique:visitors:"

	public UniqueVisit(JedisHelper helper) {
		this.jedis = helper.getConnection()
	}

	public void visit(int userNo) {
		Pipeline p = jedis.pipelined()
		p.incr(KEY_PAGE_VIEW + getToday())
		p.setbit(KEY_UNIQUE_VISITOR + getToday(), userNo, true)
		p.sync()
	}

	/**
	 * 페이지 방문자수를 리턴한다.
	 * @param date
	 * @return
	 */
	public int getPVCount(String date) {
		int result = 0
		try {
			result = Integer.parseInt(jedis.get(KEY_PAGE_VIEW + date))
		} catch (Exception e) {
			result = 0
		}
		return result
	}

	public Long getUVCount(String date) {
		return jedis.bitcount(KEY_UNIQUE_VISITOR + date)
	}

	private String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd")
		return sdf.format(new Date())
	}
}
