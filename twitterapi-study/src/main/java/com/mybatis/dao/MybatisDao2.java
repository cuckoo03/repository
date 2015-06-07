package com.mybatis.dao;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.hibernate.vo.Types;

@Repository
public class MybatisDao2 extends SqlSessionDaoSupport {
	@Resource(name = "sqlSessionTemplate2")
	// @Qualifier("sqlSessionTemplate2")
	private SqlSession sqlSession;

	@SuppressWarnings("unchecked")
	public List<Types> select() {
		return sqlSession.selectList("sql.resources.mapper.select");
	}

	public int update() {
		Random r = new Random();
		return sqlSession.update("sql.resources.mapper.update", r.nextInt(100));
	}

	public int insert() {
		Random r = new Random();
		return sqlSession.insert("sql.resources.mapper.insert", r.nextInt(100));
	}

	public int delete() {
		Random r = new Random();
		return sqlSession.delete("sql.resources.mapper.delete", r.nextInt(100));
	}
}
