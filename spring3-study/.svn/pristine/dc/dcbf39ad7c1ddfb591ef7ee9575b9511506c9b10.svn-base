package org.mybatis.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;

public class MybatisDao extends SqlSessionDaoSupport {
	public void select() {
		getSqlSession().selectList("sql.resources.mapper.select");
		System.out.println();
	}
}
