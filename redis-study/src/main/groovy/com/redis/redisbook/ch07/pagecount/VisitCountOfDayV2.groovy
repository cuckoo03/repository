package com.redis.redisbook.ch07.pagecount

import java.text.SimpleDateFormat

import redis.clients.jedis.Jedis

import com.redis.redisbook.ch07.JedisHelper

/**
 * 해시 데이터를 사용한 날짜별 이벤트 페이지 방문 횟수 저장
 * @author cuckoo03
 *
 */
class VisitCountOfDayV2 {
	private JedisHelper helper
	private Jedis jedis
	private static final String KEY_EVENT_DAILY_CLICK_TOTAL =
	"event:daily:click:total:hash:"
	private static final String KEY_EVENT_CLICK_CLICK =
	"event:daily:click:hash:"

	public VisitCountOfDayV2(JedisHelper helper) {
		this.helper = helper
		jedis = this.helper.getConnection()
	}

	public Long addVisit(String eventId) {
		this.jedis.hincrBy(KEY_EVENT_DAILY_CLICK_TOTAL, getToday(), 1)
		return this.jedis.hincrBy(KEY_EVENT_CLICK_CLICK + eventId, getToday(), 1)
	}

	public SortedMap<String, String> getVisitCountByDailyTotal() {
		SortedMap<String, String> result = new TreeMap<String, String>()
		result.putAll(this.jedis.hgetAll(KEY_EVENT_DAILY_CLICK_TOTAL))
		return result
	}
	
	public SortedMap<String, String> getVisitCountByDaily(String eventId) {
		SortedMap<String, String> result = new TreeMap<>()
		result.putAll(this.jedis.hgetAll(KEY_EVENT_CLICK_CLICK + eventId))
		return result
	}

	private String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd")
		return sdf.format(new Date())
	}
}
