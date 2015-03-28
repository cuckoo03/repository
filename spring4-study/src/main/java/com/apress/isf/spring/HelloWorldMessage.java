package com.apress.isf.spring;

public class HelloWorldMessage implements MessageService {

	@Override
	public String getMessage() {
		return "Hello";
	}
}
