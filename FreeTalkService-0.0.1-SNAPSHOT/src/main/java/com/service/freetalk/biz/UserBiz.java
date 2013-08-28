package com.service.freetalk.biz;

import java.util.List;
import java.util.Map;

import com.service.freetalk.dao.UserDao;

public class UserBiz {
	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public String loginUser(Map<String, String> param) {
		return userDao.loginUser(param);
	}

	public List findUserList(String userId) {
		return userDao.findUserList(userId);
	}

	public int registerId(String regId, String userId) {
		return userDao.registerId(regId, userId);
	}

	public void unregisterID(String regId) {
		// TODO Auto-generated method stub
	}

	public String findRegId(String receiverId) {
		return userDao.findRegId(receiverId);
	}
}
