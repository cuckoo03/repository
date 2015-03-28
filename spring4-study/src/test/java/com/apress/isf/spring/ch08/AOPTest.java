package com.apress.isf.spring.ch08;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-context.xml")
public class AOPTest {
	@Autowired
	private SearchEngine engine;

	@Test
	public void test() {
		 engine.print();
	}
}
