package com.service.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.service.hibernate.manytoone.Order;
import com.service.hibernate.manytoone.Product;

/**
 * 외부키를 이용한 n-1관계
 * 오류 발생
 * @author cuckoo03
 *
 */
public class ManyToOne {

	public static void main(String[] args) {
		SessionFactory factory = null;
		factory = new Configuration().configure().buildSessionFactory();

		Session session = factory.openSession();
		Transaction tx = null;
		tx = session.beginTransaction();

		Product p = new Product();
		Order o = new Order();
		o.setProduct(p);
		
//		session.save(o);
		
		tx.commit();
		session.close();
	}

}
