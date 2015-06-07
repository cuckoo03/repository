package com.mybatis.service;

import java.util.List;

import com.hibernate.vo.Types;

public interface IMybatisService {
	int update();

	int insert();

	int delete();

	List<Types> select();

}
