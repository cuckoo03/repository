package com.service.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.service.hibernate.manytomany.Person;
import com.service.hibernate.manytomany.Semina;

public class ManyToMany {

	/**
	 * 다대다 관계
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SessionFactory factory = null;
		factory = new Configuration().configure().buildSessionFactory();

		Session session = factory.openSession();
		Transaction tx = null;
		tx = session.beginTransaction();

		Person p = new Person();
		
		Semina s = new Semina();
		
		p.addSemina(s);
		s.addPerson(p);
		
		 session.save(p);

		tx.commit();

		session.close();
	}
}
