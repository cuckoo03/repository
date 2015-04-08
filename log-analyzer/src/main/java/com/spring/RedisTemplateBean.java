package com.spring;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

@SuppressWarnings("restriction")
public class RedisTemplateBean {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOps;
	@Resource(name = "redisTemplate")
	private SetOperations<String, String> setOps;
	//Set
	//ZSet
	//Hash
	//BoundValue
	//BoundList
	//BoundSet
	//BoundZSet
	//BoundHash

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public ValueOperations<String, String> getValueOps() {
		return valueOps;
	}
	public SetOperations<String, String> getSetOps() {
		return setOps;
	}
}