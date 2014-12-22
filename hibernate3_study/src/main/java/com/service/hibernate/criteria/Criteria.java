package com.service.hibernate.criteria;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;

import com.service.hibernate.hql.model.T1;
import com.service.hibernate.onetomany.Customer;
import com.service.hibernate.onetomany.Support;

public class Criteria {

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

			// select(session, tx);
			// select1(session, tx);
			// select2(session, tx);
			// select3(session, tx);

			Customer c1 = new Customer();
			c1.setId("1");
			c1.setPassword("password");
			c1.setName("name");
			c1.setTel("tel");

			Support s1 = new Support();
			s1.setTitle("title");

			c1.addSupport(s1);
			
			session.save(c1);
			session.save(s1);
			
//			select4(session, tx);
			
			Dept dept = new Dept();
			Emp emp1 = new Emp();
			dept.addEmp(emp1);
			
			session.save(dept);
//			select5(session, tx);
		} finally {
			tx.commit();
			session.close();
		}
	}

	// select
	private static void select(Session session, Transaction tx) {
		org.hibernate.Criteria c = session.createCriteria(T1.class);
		List list = c.list();
		System.out.println("-------------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			T1 t1 = (T1) list.get(i);
			System.out.println(t1.getName());
		}
	}

	// use expression
	private static void select1(Session session, Transaction tx) {
		org.hibernate.Criteria c = session.createCriteria(T1.class);
		c.add(org.hibernate.criterion.Expression
				.eq("name", new String("name1")));
		c.add(org.hibernate.criterion.Expression.isNull("name"));
		List list = c.list();
		System.out.println("-------------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			T1 t1 = (T1) list.get(i);
			System.out.println(t1.getName());
		}
	}

	// and, or expression
	private static void select2(Session session, Transaction tx) {
		org.hibernate.Criteria c = session.createCriteria(T1.class);
		// where (name=name1 and id < id1) or (name > name10 and id = id1)

		LogicalExpression le = Expression.and(Expression.eq("name", "name1"),
				Expression.eq("id", "id1"));
		Junction j = Expression.disjunction()
				.add(Expression.eq("name", "name1"))
				.add(Expression.eq("id", "id1"));

		c.add(Expression.or(le, j));

		List list = c.list();
		System.out.println("-------------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			T1 t1 = (T1) list.get(i);
			System.out.println(t1.getName());
		}
	}

	// sorting, paging expression
	private static void select3(Session session, Transaction tx) {
		org.hibernate.Criteria c = session.createCriteria(T1.class);
		c.addOrder(Order.desc("name"));
		c.addOrder(Order.asc("id"));
		c.setFirstResult(10);

		List list = c.list();
		System.out.println("-------------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			T1 t1 = (T1) list.get(i);
			System.out.println(t1.getName());
		}
	}

	// inner join 1:N(Customer, Support)
	private static void select4(Session session, Transaction tx) {
		org.hibernate.Criteria c = session.createCriteria(Support.class, "s");
		c.createCriteria("customer", "c");

		List list = c.list();
		System.out.println("-------------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	private static void select5(Session session, Transaction tx) {
		org.hibernate.Criteria c = session.createCriteria(Emp.class, "e");
//		c.createCriteria("customer", "c");

//		List list = c.list();
		System.out.println("-------------------------------------------------");
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i));
//		}
	}
}
