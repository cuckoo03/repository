package com.apress.isf.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class HelloWorldController {
	@RequestMapping("/")
	@ResponseBody
	String getMessage() {
		return "hello";
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldController.class, args);
	}

}
