package com.redis.redisbook.ch05;

import redis.clients.jedis.Jedis;

public class HelloJedis {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("210.122.11.210", 6379);
		jedis.auth("tapaman");
		
		String result = jedis.set("redisbook", "Hello jedis");
		System.out.println(result);
		System.out.println(jedis.get("redisbook"));
	}

}
