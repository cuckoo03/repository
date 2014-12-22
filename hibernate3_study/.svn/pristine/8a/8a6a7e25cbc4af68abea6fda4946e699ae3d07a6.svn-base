package com.service.hibernate.hql;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.service.hibernate.hql.model.T1;

public class Select {

	public static void main(String[] args) {
		SessionFactory factory = null;
		Session session = null;
		Transaction tx = null;
		try {
			factory = new Configuration().configure().buildSessionFactory();
			session = factory.openSession();
			tx = session.beginTransaction();

			for (int i = 0; i < 10; i++) {
				T1 t1 = new T1();
				t1.setName("name" + i);
				t1.setId("id" + i);
				session.save(t1);
			}

			Query query = session.createQuery("from T1 as t1 where t1.name=?");
			query.setString(0, "1");
			List<T1> list = query.list();

			for (T1 obj : list) {
				System.out.println("obj:" + obj.getName());
			}

			query = session
					.createQuery("select t1.name, t1.id from T1 as t1 where 1=1 order by t1.id desc");
			query.setFirstResult(5);
			query.setMaxResults(2);
			List list2 = query.list();

			for (int i = 0; i < list2.size(); i++) {
				Object[] row = (Object[]) list2.get(i);
				System.out.println("row:" + row[0]);
				System.out.println("row:" + row[1]);
			}
		} finally {
			tx.commit();
			session.close();
		}
	}

}
