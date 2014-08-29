package com.redis.redisbook.ch05;

import redis.clients.jedis.Jedis;

public class HelloJedis {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.1.105", 6379);
		
		String result = jedis.set("redisbook", "Hello jedis");
		System.out.println(result);
		System.out.println(jedis.get("redisbook"));
	}

}
