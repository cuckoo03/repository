package com.mybatis.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mybatis.dao.MybatisDao2;

@Service
public class MybatisService implements IMybatisService {
	@Resource(name = "mybatisDao2")
	private MybatisDao2 dao;

	public int update() {
		return dao.update();
	}
}
