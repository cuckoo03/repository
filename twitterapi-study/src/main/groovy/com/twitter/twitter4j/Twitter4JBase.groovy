package com.twitter.twitter4j

import groovy.transform.TypeChecked;

import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericXmlApplicationContext

import twitter4j.conf.ConfigurationBuilder

import com.spring.ConfigProperty

@TypeChecked
class Twitter4JBase {
	protected ConfigurationBuilder cb

	public Twitter4JBase() {
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:local/spring/application-context.xml")
		ConfigProperty p = context.getBean("configProperty",
				ConfigProperty.class)
		def debug = Boolean.valueOf(p.get("debug"))
		def consumerKey = p.get("oauth.consumerKey")
		def consumerSecret= p.get("oauth.consumerSecret")
		def accessToken = p.get("oauth.accessToken")
		def accessTokenSecret = p.get("oauath.accessTokenSecret")
		cb = new ConfigurationBuilder()
		cb.setDebugEnabled(debug).setOAuthConsumerKey(consumerKey).
				setOAuthConsumerSecret(consumerSecret).
				setOAuthAccessToken(accessToken).
				setOAuthAccessTokenSecret(accessTokenSecret)
	}
}
