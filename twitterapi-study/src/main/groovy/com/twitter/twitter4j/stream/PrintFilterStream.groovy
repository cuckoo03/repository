package com.twitter.twitter4j.stream

import groovy.transform.TypeChecked

import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericXmlApplicationContext

import twitter4j.FilterQuery
import twitter4j.StallWarning
import twitter4j.Status
import twitter4j.StatusDeletionNotice
import twitter4j.StatusListener
import twitter4j.TwitterStreamFactory
import twitter4j.auth.AccessToken

import com.spring.ConfigProperty

@TypeChecked
class PrintFilterStream {
	static main(args) {
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:local/spring/application-context.xml")
		ConfigProperty p = context.getBean("configProperty",
				ConfigProperty.class)
		def consumerKey = p.get("oauth.consumerKey")
		def consumerSecret= p.get("oauth.consumerSecret")
		def accessToken = p.get("oauth.accessToken")
		def accessTokenSecret = p.get("oauath.accessTokenSecret")

		def twitterStream = new TwitterStreamFactory().getInstance()
		twitterStream.setOAuthConsumer(consumerKey, consumerSecret)
		twitterStream.setOAuthAccessToken(new AccessToken(accessToken,
				accessTokenSecret))

		int count = 0
		def listener = new StatusListener() {
					@Override
					public void onStatus(Status status) {
						System.out.println(count++ + " @" + status.getUser().getScreenName()
								+ " - " + status.getText())
						sleep(100)
					}
					@Override
					public void onDeletionNotice(StatusDeletionNotice
							statusDeletionNotice) {
						System.out.println("Got a status deletion notice id:" +
								statusDeletionNotice.getStatusId());
					}
					@Override
					public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
						System.out.println("Got track limitation notice:" +
								numberOfLimitedStatuses);
					}
					@Override
					public void onScrubGeo(long userId, long upToStatusId) {
						System.out.println("Got scrub_geo event userId:" +
								userId + " upToStatusId:" + upToStatusId);
					}
					@Override
					public void onStallWarning(StallWarning warning) {
						System.out.println("Got stall warning:" + warning);
					}
					@Override
					public void onException(Exception ex) {
						ex.printStackTrace();
					}
				}

		twitterStream.addListener(listener)
		long[] followArray = []
		def track = new ArrayList<String>()
		track[0] = "빅데이터"
		def trackArray = (String[])track.toArray()
		def lang = (String[])["ko"].toArray()
		twitterStream.filter(new FilterQuery(0, followArray, trackArray, null,
				lang))
	}
}
