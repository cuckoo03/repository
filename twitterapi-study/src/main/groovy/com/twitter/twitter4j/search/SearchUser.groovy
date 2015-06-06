package com.twitter.twitter4j.search

import groovy.transform.TypeChecked;

import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericXmlApplicationContext

import twitter4j.ResponseList
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.User
import twitter4j.conf.ConfigurationBuilder

import com.spring.ConfigProperty

/**
 * 트위터 토큰 리스트를 조회하여 정상적으로 사용가능한 토큰과 사용가능하지 못하는 토큰을 구분하여 파일을 생성한다.
 * @author cuckoo03
 *
 */
@TypeChecked
class SearchUser {
	static main(args) {
		//		block "390174007"
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:local/spring/application-context.xml");
		ConfigProperty p = context.getBean("configProperty",
				ConfigProperty.class);
		String consumerKey = p.get("oauth.consumerKey");
		String consumerSecret = p.get("oauth.consumerSecret");
		String accessToken = p.get("oauth.accessToken");
		String accessTokenSecret = p.get("oauath.accessTokenSecret");

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessTokenSecret);

		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		def dir = System.getProperty("user.dir");
		def blockUsers = new File(dir + "/blockUser.txt")
		def normalUsers = new File(dir + "/normalUser.txt")

		new File(dir +"/tokenlist.txt").each { line ->
			String s = line
			if (s.indexOf("tokens.add") > -1) {
				def start = s.indexOf("\"") + 1
				def end = s.indexOf("-")
				def id = s.substring(start, end)
				println id

				try {
					sleep(5000)
					def u = twitter.showUser(Long.valueOf(id))
					println "screenName:" + u.getScreenName()
					normalUsers << id + "\n"
				} catch (TwitterException te) {
					if (te.getErrorCode() == 63) {
						te.printStackTrace();
						// block id
						blockUsers << id + "\n"
					} else if (te.getErrorCode() == 88) {
						te.printStackTrace();
						// request limit
						// wait 15 minutes
						println "request limit, wait 15 minutes."
						sleep(900000)
					}

					System.out.println("Failed to search users: " + te.getMessage());
				}
			}
		}
	}
}
