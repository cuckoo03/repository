package com.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class RedisDataTest {
	private RedisTemplateBean redisTemplateBean;

	public void test() {
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml");

		ConfigProperty p = context.getBean("configProperty",
				ConfigProperty.class);
		System.out.println(p.get("kafka.host"));
		
		redisTemplateBean = context.getBean("redisTemplateBean",
				RedisTemplateBean.class);
		System.out.println("------" + redisTemplateBean.getRedisTemplate());
		System.out.println("------" + redisTemplateBean.getValueOps());
		redisTemplateBean.getValueOps().set("key1", "value1");
		System.out.println(redisTemplateBean.getValueOps().get("key1"));
	}

	public static void main(String[] args) throws Exception {
		RedisDataTest data = new RedisDataTest();
		data.test();
	}
}