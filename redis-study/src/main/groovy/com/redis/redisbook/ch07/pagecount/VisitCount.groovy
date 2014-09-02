package com.redis.redisbook.ch07.pagecount

import redis.clients.jedis.Jedis

import com.redis.redisbook.ch07.JedisHelper

/**
 * 이벤트 방문 횟수 예제
 * @author cuckoo03
 *
 */
class VisitCount {
	private JedisHelper helper
	private Jedis jedis
	private static final String KEY_EVENT_CLICK_TOTAL = "event:click:total"
	private static final String KEY_EVENT_CLICK = "event:click:"
	
	public VisitCount(JedisHelper helper) {
		this.helper = helper
		this.jedis = this.helper.getConnection()
	}
	
	public Long addVisit(String eventId) {
		this.jedis.incr(KEY_EVENT_CLICK_TOTAL)
		return this.jedis.incr(KEY_EVENT_CLICK + eventId)
	}
	
	public String getVisitTotalCount() {
		return this.jedis.get(KEY_EVENT_CLICK_TOTAL)
	}
	
	public List<String> getVisitCount(String... eventList) {
		List<String> result = new ArrayList<String>()
		for (String event : eventList) {
			result.add(this.jedis.get(KEY_EVENT_CLICK + event))
		}
		
		return result
	}
	
	public Integer deleteVisitCount(String eventId) {
		return this.jedis.del(KEY_EVENT_CLICK + eventId)
	}
	
	public Integer deleteVisitTotal() {
		return this.jedis.del(KEY_EVENT_CLICK_TOTAL)
	}
}
