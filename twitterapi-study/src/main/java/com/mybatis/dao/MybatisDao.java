package com.mybatis.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hibernate.vo.Types;

public class MybatisDao extends SqlSessionDaoSupport {
	@SuppressWarnings("unchecked")
	public List<Types> select() {
		return getSqlSession().selectList("sql.resources.mapper.select");
	}
}
