package com.apress.isf.spring.ch07;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-context.xml")
@ProfileValueSourceConfiguration(CustomProfile.class)
public class CustomProfileTest {
	@Autowired
	private SearchEngineService engine;

	@IfProfileValue(name="env", values="dev")
	@Test
	public void test() {
		System.out.println(engine.getName());
	}
}
