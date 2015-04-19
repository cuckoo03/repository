package com.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hibernate.dao.HibernateDao;
import com.mybatis.dao.MybatisDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:local/spring/application-context.xml")
public class SpringTest {
	@Autowired
	private HibernateDao hibernateDao;

	@Autowired
	private MybatisDao mybatisDao;

	@Test
	public void daoTest() {
		System.out.println(hibernateDao.getTypes());
		System.out.println(mybatisDao.select());
	}
}