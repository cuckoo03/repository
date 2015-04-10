package com.twitter.twitter4j

import groovy.transform.TypeChecked

import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericXmlApplicationContext

import com.spring.ConfigProperty;

import twitter4j.Status
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

@TypeChecked
class ShowStatus {
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

		try {
			TwitterFactory tf = new TwitterFactory(cb.build())
			Twitter twitter = tf.getInstance()

			// write tweet
			def status = twitter.updateStatus("6")
			println status

			//destry tweet
			status = twitter.destroyStatus(status.getId())
			println status

			// get timeline
			def timeline = twitter.getHomeTimeline()
			timeline.each ({t ->
				println t.getUser()
				println t.getText()
			})

		} catch (TwitterException e) {
			e.printStackTrace()
		}
	}
}