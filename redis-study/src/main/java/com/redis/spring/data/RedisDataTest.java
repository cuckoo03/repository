package com.redis.spring.data;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class RedisDataTest {
	private RegistrationBean bean;

	public void test() {
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:application-context.xml");

		bean = context.getBean("registrationBean", RegistrationBean.class);
		System.out.println("------" + bean.getRedisTemplate());
		System.out.println("------" + bean.getValueOps());
		bean.getValueOps().set("key1", "value1");
		System.out.println(bean.getValueOps().get("key1"));
	}

	public static void main(String[] args) throws Exception {
		RedisDataTest data = new RedisDataTest();
		data.test();
	}
}
