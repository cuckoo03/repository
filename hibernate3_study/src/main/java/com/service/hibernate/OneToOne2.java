package com.service.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.service.hibernate.onetoone2.Member;
import com.service.hibernate.onetoone2.MemberDetail;

public class OneToOne2 {
	/**
	 * 일대일 관계(외부키를 이용한 1-1 관계)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SessionFactory factory = null;
		factory = new Configuration().configure().buildSessionFactory();

		Session session = factory.openSession();
		Transaction tx = null;
		tx = session.beginTransaction();

		Member m = new Member();
		MemberDetail detail = new MemberDetail();
		m.setDetail(detail);
		session.save(m);
		session.save(detail);
		
		Member m2 = new Member();
		MemberDetail detail2 = new MemberDetail();
		m2.setDetail(detail2);
		session.save(m2);
		
		session.delete(m);
		
		tx.commit();
		session.close();
	}
}
