package com.apress.isf.spring.ch12;

import org.springframework.stereotype.Repository;

@Repository("documentDAO")
public class DocumentRepository implements DocumentDAO {

	@Override
	public String getAll() {
		return "getAll";
	}

}
