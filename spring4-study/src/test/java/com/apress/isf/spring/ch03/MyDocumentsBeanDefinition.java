package com.apress.isf.spring.ch03;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;

public class MyDocumentsBeanDefinition {
	@Test
	public void setup() {
		ApplicationContext context = new GenericGroovyApplicationContext(
				"mydocuments.groovy");
	}
}
