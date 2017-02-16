package com.twitter.twitter4j.search.friends

import groovy.transform.TypeChecked
import twitter4j.Query
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory

import com.twitter.twitter4j.Twitter4JBase
/**
 * 수집원 프렌즈(팔로잉) 가져오기
 * @author cuckoo03
 *
 */
@TypeChecked
class FollowerSearch extends Twitter4JBase {
	public void start() {
		try {
			def twitter = new TwitterFactory(cb.build()).getInstance()

			// show follower count
			def user = twitter.showUser("kangfull74")
			println "follower count:$user.friendsCount"

			def cursor = -1
			def ids
			println "Listing folower's ids"
			while (cursor != 0) {
				println twitter.getRateLimitStatus()

				ids = twitter.getFollowersIDs("kangfull74", cursor)

				println twitter.getRateLimitStatus()

				// 한번에 가져오는 프렌즈 수 : 5000
				println "size:" + ids.getIDs().size()

				ids.getIDs().each {id ->
					print id.toString()  + " "
				}

				cursor = ids.getNextCursor()
			}
		} catch (TwitterException e) {
			e.printStackTrace()
			System.exit(0)
		}
	}

	static main(args) {
		FollowerSearch friendsSearch = new FollowerSearch()
		friendsSearch.start()
	}
}
