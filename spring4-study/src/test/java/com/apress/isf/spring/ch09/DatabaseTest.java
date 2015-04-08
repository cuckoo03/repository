package com.apress.isf.spring.ch09;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * embedded db를 이용한 예제
 * @author cuckoo03
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-context.xml")
public class DatabaseTest {
	@Autowired
	private JdbcTemplate template;

	@Test
	public void test() {
		System.out.println("**********************");
		System.out.println("**********************");
		System.out.println("**********************");
		List<Map<String, Object>> list = template
				.queryForList("select * from types");
		for (Map<String, Object> item : list) {
			System.out.println(item);
		}
	}
}
