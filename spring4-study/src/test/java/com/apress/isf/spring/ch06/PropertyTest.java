package com.apress.isf.spring.ch06;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PropertyTest {
	@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"spring/application-context.xml");
		UserService user = context.getBean(UserService.class);
		user.print();
	}
}
