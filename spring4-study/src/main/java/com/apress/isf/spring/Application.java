package com.apress.isf.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class Application {

	@Bean
	MessageService messageService() {
		return new HelloWorldMessage();
	}

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(
				Application.class);
		
		MessageService service = context.getBean(MessageService.class);
		System.out.println(service.getMessage());
	}
}
