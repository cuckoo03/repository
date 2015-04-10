package com.twitter.twitter4j

import groovy.transform.TypeChecked
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationBuilder

@TypeChecked
class UpdateStatus {

	static main(args) {
		def consumerKey = "WKzK4qYGZYuoOEYcnS9L50Gej"
		def consumerSecret= "XZGzi1stEoGC4ZgQlSS1IT1yzbsGUIz7J1igV9AMICUl7SrgzG"
		def accessToken = "3147697626-sn2hFfU81AxKN33y6qBbDChO98YptDPfelnq1I3"
		def accessTokenSecret = "9R8RqJqfJkiPAmtUcop1BthbrEzUez6s3hF6Gy4hKOXEK"

		ConfigurationBuilder cb = new ConfigurationBuilder()
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).
				setOAuthConsumerSecret(consumerSecret).
				setOAuthAccessToken(accessToken).
				setOAuthAccessTokenSecret(accessTokenSecret)

		TwitterFactory tf = new TwitterFactory(cb.build())
		Twitter twitter = tf.getInstance()

		// 이미 액세스 토큰 발생시 런타임 익셉션 발생
		RequestToken requestToken = twitter.getOAuthRequestToken()
		println "request token:" + requestToken.getToken()
		println "request token secret:" + requestToken.getTokenSecret()

	}
}