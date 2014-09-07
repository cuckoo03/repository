package com.redis.redisbook.ch08.replication

import com.redis.redisbook.ch07.redislogger.KeyMaker;

/**
 * 복제 확인 테스트를 위한 키 메이커
 * @author cuckoo03
 *
 */
class ReplicationKeyMaker implements KeyMaker {
	private static final String keyPrefix = "Replication-"
	private int index

	public ReplicationKeyMaker(int index) {
		this.index = index
	}

	public String getKey() {
		return keyPrefix + index
	}
}
