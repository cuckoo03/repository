package com.redis.redisbook.ch07.like

import redis.clients.jedis.Jedis
import redis.clients.jedis.Pipeline;

import com.redis.redisbook.ch07.JedisHelper

/**
 * 좋아요 처리 예제
 * @author cuckoo03
 *
 */
class LikePosting {
	private Jedis jedis
	private static final String KEY_LIKE_SET = "posting:like:"
	
	public LikePosting(JedisHelper helper) {
		this.jedis = helper.getConnection()
	}
	
	public boolean like(String postingNo, String userNo) {
		return this.jedis.sadd(KEY_LIKE_SET + postingNo, userNo) > 0
	}
	
	public boolean unlike(String postingNo, String userNo) {
		return this.jedis.srem(KEY_LIKE_SET + postingNo, userNo) > 0
	}
	
	public boolean isLiked(String postingNo, String userNo) {
		return this.jedis.sismember(KEY_LIKE_SET + postingNo, userNo)
	}
	
	public boolean deleteLikeInfo(String postingNo) {
		return this.jedis.del(KEY_LIKE_SET + postingNo) > 0
	}
	
	public Long getLikeCount(String postingNo) {
		return this.jedis.scard(KEY_LIKE_SET + postingNo)
	}
	
	public List<Long> getLikeCountList(String[] postingList) {
		List<Long> result = new ArrayList<>()
		
		Pipeline p = this.jedis.pipelined()
		postingList.each {
			p.scard(KEY_LIKE_SET + it)
		}
		List<Object> pipelineResult = p.syncAndReturnAll()
		
		pipelineResult.each {
			result.add(it)
		}
		
		return result
		
	}
}
