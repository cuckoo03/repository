package com.elasticsearch.client.dao

import java.util.HashMap;
import java.util.Map;

import groovy.transform.TypeChecked

import javax.annotation.Resource

import org.apache.ibatis.session.SqlSession
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository

import com.elasticsearch.client.entity.TableEntity;

@TypeChecked
@Repository
class TapacrossDao {
	@Resource(name = "sqlSessionTemplate")
	private SqlSession sqlSession;

	public List<TableEntity> selectArticles(int startNum, int endNum, 
		String tableName)
	throws DataAccessException {
		Map<String, Object> paramMap = [:]
		paramMap.put("startNum", startNum);
		paramMap.put("endNum", endNum);
		paramMap.put("tableName", tableName);

		return sqlSession
				.selectList("sql.resources.dao.selectArticles", paramMap);
	}
}
