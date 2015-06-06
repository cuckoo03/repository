package com.twitter.twitter4j.search;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import com.spring.ConfigProperty;

public class SearchUsers {
	/**
	 * Usage: java twitter4j.examples.user.SearchUsers [query]
	 *
	 * @param args
	 *            message
	 */
	public static void main(String[] args) {
		args = new String[] { "407303492" };
		if (args.length < 1) {
			System.out
					.println("Usage: java twitter4j.examples.user.SearchUsers [query]");
			System.exit(-1);
		}
		try {
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

			int page = 1;
			ResponseList<User> users;
			do {
				users = twitter.searchUsers(args[0], page);
				for (User user : users) {
					if (user.getStatus() != null) {
						System.out.println("@" + user.getScreenName() + " - "
								+ user.getStatus().getText());
					} else {
						// the user is protected
						System.out.println("@" + user.getScreenName());
					}
				}
				page++;
			} while (users.size() != 0 && page < 50);
			System.out.println("done.");
			System.exit(0);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search users: " + te.getMessage());
			System.exit(-1);
		}
	}
}
