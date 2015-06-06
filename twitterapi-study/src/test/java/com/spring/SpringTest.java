package com.spring;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hibernate.vo.Types;
import com.mybatis.dao.MybatisDao;
import com.mybatis.service.IMybatisService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:local/spring/application-context.xml")
public class SpringTest {

	// @Autowired
	// private HibernateDao hibernateDao;

	@Autowired
	private MybatisDao mybatisDao;

	@Autowired
	private IMybatisService mybatisService;

	@BeforeClass
	public static void before() {
		System.out.println();
	}

	@Test
	public void test() {
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
	}

	@Test
	@Transactional
	@Rollback
	public void daoTest() {
		// System.out.println(hibernateDao.getTypes());
		mybatisService.update();
		List<Types> result = mybatisDao.select();
		for (Types item : result) {
			System.out.println(item.getTypeId());
		}
	}
}