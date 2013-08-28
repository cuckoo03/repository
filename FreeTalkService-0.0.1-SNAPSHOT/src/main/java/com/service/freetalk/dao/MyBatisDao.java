package com.service.freetalk.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;

public class MyBatisDao extends SqlSessionDaoSupport {
	public void select() {
		try {
			String result = (String) getSqlSession().selectOne(
					"sql.resources.mapper.selectT1", null);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
