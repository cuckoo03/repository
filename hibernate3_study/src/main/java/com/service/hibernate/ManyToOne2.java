package com.service.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.service.hibernate.manytoone.Order;
import com.service.hibernate.manytoone.Product;

/**
 * join를 이용한 n-1관계
 * 오류 발생
 * @author cuckoo03
 *
 */
public class ManyToOne2 {

	public static void main(String[] args) {
		SessionFactory factory = null;
		factory = new Configuration().configure().buildSessionFactory();

		Session session = factory.openSession();
		Transaction tx = null;
		tx = session.beginTransaction();

		tx.commit();
		session.close();
	}

}
