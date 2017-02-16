package com.twitter.twitter4j.search

import groovy.transform.TypeChecked;

import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericXmlApplicationContext

import twitter4j.Query
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

import com.spring.ConfigProperty

@TypeChecked
class Search {
	static main(args) {
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:local/spring/application-context.xml")
		ConfigProperty p = context.getBean("configProperty",
				ConfigProperty.class)
		def debug = Boolean.valueOf(p.get("debug"))
		def consumerKey = p.get("oauth.consumerKey")
		def consumerSecret= p.get("oauth.consumerSecret")
		def accessToken = p.get("oauth.accessToken")
		def accessTokenSecret = p.get("oauath.accessTokenSecret")
		ConfigurationBuilder cb = new ConfigurationBuilder()
		cb.setDebugEnabled(debug).setOAuthConsumerKey(consumerKey).
				setOAuthConsumerSecret(consumerSecret).
				setOAuthAccessToken(accessToken).
				setOAuthAccessTokenSecret(accessTokenSecret)

		Twitter twitter = new TwitterFactory(cb.build()).getInstance()
		int count = 0
		try {
			def query = new Query("남성남나이")
			def result = twitter.search(query)
			while (query != null) {
				def tweets = result.getTweets()
				println result.getTweets().size()
				
				tweets.each {tweet ->
					println count++ + ":" + tweet.getUser().getScreenName() + "-" + tweet.getText()
				}
				query = result.nextQuery()
				if (query != null)
					result = twitter.search(query)
			}
			System.exit(0)
		} catch (TwitterException e) {
			e.printStackTrace()
			println e.getMessage()
			System.exit(-1)
		}
	}
}