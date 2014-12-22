package com.service.hibernate.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HibernateSpringIntegration {

	public static void main(String[] args) {
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/webApplicationContext.xml");
		HibernateDao dao = context.getBean(HibernateDao.class);
		dao.insertData();
	}

}
