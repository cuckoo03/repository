package com.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hibernate.dao.HibernateDao;
import com.mybatis.service.IMybatisService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:local/spring/application-context.xml")
public class SpringTest {

	@Autowired
	private HibernateDao hibernateDao;

	@Autowired
	private IMybatisService mybatisService;

	/**
	 * hibernate는 transaction1,2 둘다에도 적용됨
	 */
	@Test
	@Transactional("transactionManager2")
	public void hibernateTest() {
		mybatisService.insert();
		hibernateDao.addTypes();
		System.out.println(hibernateDao.getTypes().size());
	}

	@Test
	@Transactional("transactionManager2")
	public void mybatisTest() {
		mybatisService.insert();
		hibernateDao.addTypes();
		System.out.println(mybatisService.select().size());
	}
	
	@Test
	@Transactional("transactionManager2")
	public void deleteTest() {
		mybatisService.delete();
	}
}