package com.apress.isf.spring.ch12;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@EnableAutoConfiguration
@ImportResource({ "classpath*:spring/application-context.xml" })
@RequestMapping("/")
@Controller
public class DocumentController {
	@Autowired
	private DocumentService documentFacade;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getDocument() {
		System.out.println(documentFacade.getAllDocument());
		System.out.println(documentFacade.getAllDocument());
		System.out.println(documentFacade.getAllDocument());
		System.out.println(documentFacade.getAllDocument());
		System.out.println(documentFacade.getAllDocument());
		System.out.println(documentFacade.getAllDocument());
		return documentFacade.getAllDocument();
	}

	public static void main(String[] args) {
		SpringApplication.run(DocumentController.class, args);
	}
}
