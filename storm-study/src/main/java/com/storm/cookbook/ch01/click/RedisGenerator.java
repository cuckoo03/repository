package com.storm.cookbook.ch01.click;

import java.util.Random;

import org.json.simple.JSONObject;

import redis.clients.jedis.Jedis;

public class RedisGenerator {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException {
		Random rand = new Random();
		Jedis jedis = new Jedis("192.168.1.101", 6300);
		jedis.auth("12341234");

		for (int i = 0; i < 10000; i++) {
			int randomNum = rand.nextInt(10);
			String ip = "192.168.1." + randomNum;
			String client = "client" + randomNum;
			String url = "localhost" + randomNum + ".com";
			boolean bool = false;
			JSONObject json = new JSONObject();
			json.put("ip", ip);
			json.put("client-key", client);
			json.put("url", url);
			jedis.rpush("count", json.toJSONString());
		}
	}
}
