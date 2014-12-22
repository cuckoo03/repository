package com.service.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.google.gson.Gson;
import com.service.hibernate.manytomany.Semina;
import com.service.hibernate.test.Version;

public class VersionTest {

	public static void main(String[] args) {
		SessionFactory factory = null;
		factory = new Configuration().configure().buildSessionFactory();

		Session session = factory.openSession();
		Transaction tx = null;
		tx = session.beginTransaction();

		Version version = null;
		try {
			version = (Version) session
					.createQuery("from Version version order by idx desc")
					.setMaxResults(1).uniqueResult();
		} catch (HibernateException e) {
			throw e;
		} finally {
			session.close();
		}

		String toJson = new Gson().toJson(version);

		tx.commit();

		session.close();
	}

}
