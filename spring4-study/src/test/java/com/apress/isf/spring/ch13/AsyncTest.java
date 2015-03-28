package com.apress.isf.spring.ch13;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-context.xml")
public class AsyncTest {
	@Test
	public void test() throws InterruptedException {
		System.out.println("before");
		busy();
		System.out.println("after");
	}

	private void busy() throws InterruptedException {
		System.out.println("start...");
		System.out.println("end...");
	}
}
