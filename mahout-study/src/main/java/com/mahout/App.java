package com.mahout;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.spring.ConfigProperty;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml");
		ConfigProperty property = context.getBean("configProperty",
				ConfigProperty.class);
		System.out.println(property.get("mysql.driver"));
	}
}
