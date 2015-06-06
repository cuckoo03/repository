package com.hibernate.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.hibernate.vo.Types;

public class HibernateDao extends HibernateDaoSupport {
	public void addTypes() {
		Types types = new Types();
		types.setTypeId("2");
		getHibernateTemplate().save(types);
	}
	@SuppressWarnings("unchecked")
	public List<Types> getTypes() {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery("from Types");

		List<Types> list = null;
		try {
			list = query.list();
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			session.close();
			session = null;
		}
		
		return list;
	}
}
