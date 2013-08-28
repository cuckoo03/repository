package com;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.rabbitmq.client.ConnectionFactory;


/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml");
		
		ConnectionFactory factory = context.getBean(ConnectionFactory.class);
		MyClassBean myclass = context.getBean(MyClassBean.class);
	}
}
