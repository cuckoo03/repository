package com.service.hibernate.spring;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.service.hibernate.hql.join.JoinA;

public class HibernateDao extends HibernateDaoSupport {
	public void testDao() {
		JoinA a = new JoinA();
		getHibernateTemplate().save(a);
		
		List list = getHibernateTemplate().find("from JoinA as joina");
	}
	public void insertData() {
		try {
			for (int i = 103867; i < 1000000; i++) {
				T1 t = new T1();
				t.setCol1(i);
				t.setCol2(String.valueOf(i));
				t.setCol3(String.valueOf(i));
				t.setCol4(String.valueOf(i));
				t.setCol5(String.valueOf(i));
				
				getHibernateTemplate().save(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("-------------------");
	}
}
