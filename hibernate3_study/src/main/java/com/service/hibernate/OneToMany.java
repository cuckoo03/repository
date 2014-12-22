package com.service.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.service.hibernate.onetomany.Customer;
import com.service.hibernate.onetomany.Reply;
import com.service.hibernate.onetomany.Support;

public class OneToMany {

	/**
	 * 일대다 관계
	 * @param args
	 */
	public static void main(String[] args) {
		SessionFactory factory = null;
		factory = new Configuration().configure()
				.buildSessionFactory();

		Session session = factory.openSession();
		Transaction tx = null;
		tx = session.beginTransaction();

		Customer c1 = new Customer();
		c1.setId("1");
		c1.setPassword("password");
		c1.setName("name");
		c1.setTel("tel");
		
		Support s1 = new Support();
		s1.setTitle("title");
		
		c1.addSupport(s1);
		
		Reply r1 = new Reply();
		r1.setReply("reply");
		
		s1.addReply(r1);

		session.save(c1);
		session.save(s1);
		session.save(r1);

		tx.commit();
		
		session.close();
	}
}
