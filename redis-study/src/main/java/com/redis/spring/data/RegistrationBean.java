package com.redis.spring.data;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RegistrationBean {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOps;

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public ValueOperations<String, String> getValueOps() {
		return valueOps;
	}

	public void setValueOps(ValueOperations<String, String> valueOps) {
		this.valueOps = valueOps;
	}
}
