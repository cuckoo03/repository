package com.service.hibernate;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.service.hibernate.manytomany2.Category;
import com.service.hibernate.manytomany2.Item;

public class ManyToMany2 {

	/**
	 * 다대다 관계(양방향)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SessionFactory factory = null;
		factory = new Configuration().configure().buildSessionFactory();

		Session session = factory.openSession();
		Transaction tx = null;
		tx = session.beginTransaction();

		Category c = new Category();
		Item i = new Item();

		c.addItem(i);
		
		// item에 inverse를 활성화 하였으므로 item객체에서 매핑을 할 필요는 없다

		// 자식 mapping 삭제시
		// c.getItems().clear();

		session.save(c);
		tx.commit();
		session.close();
	}
}
