package com.spring;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigProperty {
	@Autowired
	private Properties props;

	public String get(String key) {
		return props.getProperty(key);
	}
}