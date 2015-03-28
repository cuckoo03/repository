package com.apress.isf.spring.ch08;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class AroundLoggingModule implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("AAAAAAAAAAAAAAAAAAA");
		System.out.println("AAAAAAAAAAAAAAAAAAA");
		System.out.println("AAAAAAAAAAAAAAAAAAA");
		System.out.println("AAAAAAAAAAAAAAAAAAA");
		System.out.println("AAAAAAAAAAAAAAAAAAA");
		return null;
	}

}
