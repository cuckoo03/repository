package com.redis.redisbook.ch07.uniquevisit

import java.text.SimpleDateFormat;

import com.redis.redisbook.ch07.JedisHelper;

import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis
import redis.clients.jedis.Pipeline;

/**
 * 날짜별 방문 횟수 증가 메서드가 추가된 순 방문 횟수 예제
 * @author cuckoo03
 *
 */
class UniqueVisitV2 {
	private Jedis jedis
	private static final String KEY_PAGE_VIEW = "page:view:"
	private static final String KEY_UNIQUE_VISITOR = "unique:visitors:"

	public UniqueVisitV2(JedisHelper helper) {
		this.jedis = helper.getConnection()
	}

	public void visit(int userNo) {
		Pipeline p = jedis.pipelined()
		p.incr(KEY_PAGE_VIEW + getToday())
		p.setbit(KEY_UNIQUE_VISITOR + getToday(), userNo, true)
		p.sync()
	}

	public void visit(int userNo, String date) {
		Pipeline p = jedis.pipelined()
		p.incr(KEY_PAGE_VIEW + date)
		p.setbit(KEY_UNIQUE_VISITOR + date, userNo, true)
		p.sync()
	}
	
	public Long getUVSum(String[] dateList) {
		String[] keyList = new String[dateList.length]
		int i = 0
		dateList.each {
			keyList[i] = KEY_UNIQUE_VISITOR + it
			i++
		}
		
		jedis.bitop(BitOP.AND, "uv:event", keyList)
		return jedis.bitcount("uv:event")
	}

	private String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd")
		return sdf.format(new Date())
	}
}
