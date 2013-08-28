package com.service.freetalk.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/web-application-context.xml")
public class MyBatisDaoTest {
	@Autowired
	private MyBatisDao mybatisDao;
	@Test
	public void select() {
		mybatisDao.select();
	}
	public void mytest() {
		
	}
}
