package com.service.hibernate.hql;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.service.hibernate.hql.join.JoinA;
import com.service.hibernate.hql.join.JoinB;
import com.service.hibernate.onetoone.Board;
import com.service.hibernate.onetoone.BoardDetail;

public class Join {

	public static void main(String[] args) {
		SessionFactory factory = null;
		Session session = null;
		Transaction tx = null;
		try {
			factory = new Configuration().configure().buildSessionFactory();
			session = factory.openSession();
			tx = session.beginTransaction();

			for (int i = 0; i < 10; i++) {
				JoinA a = new JoinA();
				JoinB b = new JoinB();
				a.setJoinT(b);
				b.setJoinT(a);

				session.save(a);
				session.save(b);
			}

			// join1(session, tx);
			// join2(session, tx);
			// join3(session, tx);
			// join4(session, tx);
			join5(session, tx);
		} finally {
			tx.commit();
			session.close();
		}
	}

	public static void join1(Session session, Transaction tx) {
		Query query = session.createQuery("from JoinA a");
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	// from절 조인
	public static void join2(Session session, Transaction tx) {
		Query query = session
				.createQuery("from JoinA a inner join a.joinT b where a.id = 3");
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			System.out.println("A:" + ((JoinA) obj[0]).getId());
			System.out.println("B:" + ((JoinB) obj[1]).getId());
		}
	}

	// from절에서 fetch 사용
	public static void join3(Session session, Transaction tx) {
		Query query = session
				.createQuery("from JoinA a left join fetch a.joinT b where a.id = 3");
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {
			System.out.println("A:" + ((JoinA) list.get(i)).getId());
		}
	}

	public static void join4(Session session, Transaction tx) {
		Query query = session
				.createQuery("from JoinA a left join fetch a.joinT b where a.id = 3");
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {
			System.out.println("A:" + ((JoinA) list.get(i)).getId());
		}
	}

	// catesian join
	public static void join5(Session session, Transaction tx) {
		Query query = session
				.createQuery("from JoinA a, JoinB b where b.id = 3");
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			System.out.println("A:" + ((JoinA) obj[0]).getId());
			System.out.println("B:" + ((JoinB) obj[1]).getId());
		}
	}
}
