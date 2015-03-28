package com.apress.isf.spring.ch07;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-context.xml")
@ActiveProfiles("dev")
public class ProfileTest {
	@Autowired
	private SearchEngineService engine;

	@Test
	@Repeat(10)
	public void test() {
		System.out.println(engine.getName());
	}
}
