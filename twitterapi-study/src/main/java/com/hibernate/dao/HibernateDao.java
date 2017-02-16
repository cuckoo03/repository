package com.hibernate.dao;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.hibernate.vo.Types;

@Repository
public class HibernateDao {
	@Resource(name = "hibernateTemplate")
	private HibernateTemplate template;

	public void addTypes() {
		Types types = new Types();
		Random r = new Random();
		types.setTypeId(String.valueOf(r.nextInt(100)));
		template.save(types);
	}

	@SuppressWarnings("unchecked")
	public List<Types> getTypes() {
		Session session = template.getSessionFactory().openSession();
		Query query = session.createQuery("from Types");

		List<Types> list = null;
		try {
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			session = null;
		}

		return list;
	}

	public int update() {
		Types entity = new Types();
		entity.setTypeId("2");
		template.update(entity);
		return 0;
	}
}
