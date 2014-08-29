package com.redis;

import redis.clients.jedis.JedisPool;

import com.redis.redisbook.ch07.JedisHelper;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
		JedisPool j = new JedisPool("");
    }
}
