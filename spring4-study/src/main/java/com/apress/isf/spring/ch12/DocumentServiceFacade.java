package com.apress.isf.spring.ch12;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("documentFacade")
public class DocumentServiceFacade implements DocumentService{
	@Autowired
	private DocumentDAO documentDAO;
	@Override
	public String getAllDocument() {
		return documentDAO.getAll();
	}

}
