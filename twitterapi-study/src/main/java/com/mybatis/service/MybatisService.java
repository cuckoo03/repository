package com.mybatis.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hibernate.vo.Types;
import com.mybatis.dao.MybatisDao2;

@Service
public class MybatisService implements IMybatisService {
	@Resource(name = "mybatisDao2")
	private MybatisDao2 dao;

	@Override
	public List<Types> select() {
		return dao.select();
	}

	@Override
	public int update() {
		return dao.update();
	}

	@Override
	public int insert() {
		return dao.insert();
	}

	@Override
	public int delete() {
		return dao.delete();
	}
}
