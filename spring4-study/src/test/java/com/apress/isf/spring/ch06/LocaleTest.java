package com.apress.isf.spring.ch06;

import java.util.Locale;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LocaleTest {
	@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"spring/application-context.xml");
		String eng = context.getMessage("title", null, Locale.ENGLISH);
		System.out.println(eng);
	}
}
