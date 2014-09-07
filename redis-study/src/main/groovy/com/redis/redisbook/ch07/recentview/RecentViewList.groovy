package com.redis.redisbook.ch07.recentview

import redis.clients.jedis.Jedis

import com.redis.redisbook.ch07.JedisHelper

/**
 * 최근 조회 상품 목록
 * @author cuckoo03
 *
 */
class RecentViewList {
	private Jedis jedis
	private static final String KEY_VIEW_LIST = "recent:view:"
	private static final int listMaxSize = 30
	private String userNo
	
	public RecentViewList(JedisHelper helper, String userNo) {
		this.jedis = helper.getConnection()
		this.userNo = userNo
	}
	
	public Long add(String productNo) {
		Long result = this.jedis.lpush(KEY_VIEW_LIST + this.userNo, productNo)
		this.jedis.ltrim(KEY_VIEW_LIST + this.userNo, 0, listMaxSize - 1)
		
		return result
	}
	
	public List<String> getRecentViewList() {
		return this.jedis.lrange(KEY_VIEW_LIST + this.userNo, 0, -1)
	}
	
	public List<String> getRecentViewList(int cnt) {
		return this.jedis.lrange(KEY_VIEW_LIST + this.userNo, 0, cnt - 1)
	}
	
	public int getListMaxSize() {
		return listMaxSize
	}
}
