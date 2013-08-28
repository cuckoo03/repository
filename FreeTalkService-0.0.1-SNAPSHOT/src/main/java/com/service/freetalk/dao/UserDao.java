package com.service.freetalk.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

public class UserDao extends SqlSessionDaoSupport {

	public String loginUser(Map<String, String> param) {
		@SuppressWarnings("rawtypes")
		String result = (String) getSqlSession().selectOne(
				"sql.resources.mapper.selectUser", param);
		if (null != result) {
			return "1";
		} else {
			return "0";
		}
	}

	public List findUserList(String userId) {
		@SuppressWarnings("rawtypes")
		List result = getSqlSession().selectList(
				"sql.resources.mapper.selectUserList", userId);
		return result;
	}

	public int registerId(String regId, String userId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("regId", regId);
		param.put("userId", userId);
		int result = getSqlSession().update("sql.resources.mapper.registerId",
				param);
		return result;
	}

	public String findRegId(String receiverId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("receiverId", receiverId);
		String rev = (String) getSqlSession().selectOne(
				"sql.resources.mapper.selectRegId", param);

		return rev;
	}
}
