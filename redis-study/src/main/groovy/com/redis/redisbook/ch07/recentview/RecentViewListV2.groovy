package com.redis.redisbook.ch07.recentview

import redis.clients.jedis.Jedis

import com.redis.redisbook.ch07.JedisHelper

/**
 * 정렬된 셋 데이터형을 사용한 최근 조회 상품 목록
 * @author cuckoo03
 *
 */
class RecentViewListV2 {
	private Jedis jedis
	private static final String KEY_VIEW_LIST = "recent:view:zset:"
	private static final int listMaxSize = 30
	private String key
	
	public RecentViewListV2(JedisHelper helper, String userNo) {
		this.jedis = helper.getConnection()
		this.key = KEY_VIEW_LIST + userNo
	}
	
	public Long add(String productNo) {
		Long result = jedis.zadd(this.key, System.nanoTime(), productNo)
		jedis.zremrangeByRank(this.key, -(listMaxSize + 1), -(listMaxSize + 1))
		
		return result
	}
	
	public Set<String> getRecentViewList() {
		return jedis.zrevrange(this.key, 0, -1) 
	}
	
	public Set<String> getRecentViewList(int cnt) {
		return jedis.zrevrange(this.key, 0, cnt - 1)
	}
	
	public int getListMaxSize() {
		return listMaxSize
	}
}
