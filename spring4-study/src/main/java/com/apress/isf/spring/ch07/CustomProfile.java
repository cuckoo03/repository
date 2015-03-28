package com.apress.isf.spring.ch07;

import org.springframework.test.annotation.ProfileValueSource;

public class CustomProfile implements ProfileValueSource {

	@Override
	public String get(String key) {
		if (key.equals("env")) {
			return "dev";
		}
		return "dev";
	}
}
