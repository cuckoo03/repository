package com.mybatis.dao;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.hibernate.vo.Types;

@Repository
public class MybatisDao extends SqlSessionDaoSupport {
	@Resource(name = "sqlSessionTemplate")
	private SqlSession sqlSession;

	@SuppressWarnings("unchecked")
	public List<Types> select() {
		return sqlSession.selectList("sql.resources.mapper.select");
	}

	public int insert() {
		Random r = new Random();
		return sqlSession.insert("sql.resources.mapper.insert", r.nextInt(100));
	}
}
