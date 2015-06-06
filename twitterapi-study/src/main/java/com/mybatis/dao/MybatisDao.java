package com.mybatis.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hibernate.vo.Types;

public class MybatisDao extends SqlSessionDaoSupport {
	@Resource(name = "sqlSessionTemplate")
	private SqlSession sqlSession;

	@SuppressWarnings("unchecked")
	public List<Types> select() {
		return sqlSession.selectList("sql.resources.mapper.select");
	}
}
