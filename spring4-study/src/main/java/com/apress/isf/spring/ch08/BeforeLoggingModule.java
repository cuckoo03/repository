package com.apress.isf.spring.ch08;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

public class BeforeLoggingModule implements MethodBeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbb");
		System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbb");
		System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbb");
		System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbb");
		System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbb");
		System.out.println(method.getName() + "," + args + "," + target);
	}
}
