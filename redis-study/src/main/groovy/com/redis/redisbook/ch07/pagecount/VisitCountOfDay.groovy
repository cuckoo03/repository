package com.redis.redisbook.ch07.pagecount

import java.text.SimpleDateFormat

import redis.clients.jedis.Jedis

import com.redis.redisbook.ch07.JedisHelper

/**
 * 날짜별 이벤트 페이지 방문 횟수 저장과 조회
 * @author cuckoo03
 *
 */
class VisitCountOfDay {
	private JedisHelper helper
	private Jedis jedis
	private static final String KEY_EVENT_CLICK_DAILY_TOTAL =
	"event:click:daily:total:"
	private static final String KEY_EVENT_CLICK_DAILY =
	"event:click:daily:"

	public VisitCountOfDay(JedisHelper helper) {
		this.helper = helper
		this.jedis = helper.getConnection()
	}

	public Long addVisit(String eventId) {
		this.jedis.incr(KEY_EVENT_CLICK_DAILY_TOTAL + getToday())
		return this.jedis.incr(KEY_EVENT_CLICK_DAILY + getToday() + ":" +
		eventId)
	}

	public String getVisitTotalCount(String date) {
		return this.jedis.get(KEY_EVENT_CLICK_DAILY_TOTAL + date)
	}
	
	/**
	 * 리스트에 널이 포함될 수 있음
	 * @param eventId
	 * @param dateList
	 * @return
	 */
	public List<String> getVisitCountByDate(String eventId, String[] dateList) {
		List<String> result = new ArrayList<>()
		dateList.each { it ->
			result.add(jedis.get(KEY_EVENT_CLICK_DAILY + it + ":" + eventId))
		}
		
		return result
	}

	private String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd")
		return sdf.format(new Date())
	}
}
