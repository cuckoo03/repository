package com.facebook

import org.junit.Test

import facebook4j.Facebook
import facebook4j.FacebookFactory
import facebook4j.Photo
import facebook4j.Post
import facebook4j.Reading
import facebook4j.ResponseList
import facebook4j.auth.AccessToken
import groovy.transform.TypeChecked

@TypeChecked
class FacebookTest {
	def Facebook facebook
	def String siteId = "100002495957059"
	// 교보 "370171536337573"
	// 100002495957059 ko
	// "100000295404455" //friend
	// 100001221615136 // https://www.facebook.com/egene.koo

	@Test
	public void test() {
		this.facebook = new FacebookFactory().getInstance();
				facebook.setOAuthAppId("333944576801640", "211e40b810647d73d05dc66c04fa2603");
//				facebook.setOAuthAppId("407092052822104", "ee5c1a295f8a1b88ff91f97627450a46");
//		facebook.setOAuthAppId("407620682769241", "0b8cc7789e8585208d132bff9248b706");
		// test app
		def accessTokenString = facebook.getOAuthAppAccessToken().getToken();
		//		facebook.setOAuthAccessToken(new AccessToken(accessTokenString));

		// user access token
//		facebook.setOAuthAccessToken(new AccessToken("CAAFyP4EzCFgBAA7BROYE4DioRxITlUHulTls4r9eEcvob7X6SbE7ha1OXbNlgYXcO1gDziYBlh13KHd0M1UVZC3OcLUjoZCZAYeOLjNlA6uFG57TpOEqF5HL6jCJv4IAJGBYJSmardC5ZBZCMZA24tkQZB0gBrsZBmYHmllHTe2ZAvpPkAiYnSv5fMNFCIIRdFwQcl7U4I6sLhRI3ppPhVXwhd3seYJjt35kZD"))
		

		// success
		//		feed()
//		getPermissions()
		getUsers()
				accounts()
//				getUserLikes()
		//		getPosts()

		//token required
//		getFriends()
		//		getFamily()
		//		getPhoto()
//				getMe()
		//getAlbums()
		//getVideos()
		//getTelevision()
		//getGames()
		//getMovies()
		//getMusics()

		// another token
		//		searchUsers()
	}

	def void getPosts() {
		println "posts:" + facebook.getPosts()
	}

	// access token required
	// profile photo
	def void getPhoto() {
		def photos = facebook.getPhotos(siteId)
		println "photo:" + photos.size()
		//		photos.each {photo ->
		//			println photo
		//			println "-"
		//		}
	}

	// succcess, permission
	def void getMusics() {
		def music = facebook.getMusic(siteId)
		println music
	}

	// success
	def void getMovies() {
		println facebook.getMovies(siteId)
	}

	// success, userid
	def void getGames() {
		println facebook.getGames(siteId)
	}

	// success, not page,
	def void getTelevision() {
		println facebook.getTelevision(siteId)
	}

	// success, user token required
	def void getVideos() {
		def videos = facebook.getVideos(siteId)
		println videos.size()
	}

	// fail, application
	def void getPermissions() {
		def permissions = facebook.getPermissions(siteId)
		permissions.each {permission -> println "permission:" + permission }

	}


	// success
	// 페이지가 좋아함 체크한 페이지 목록
	def void getUserLikes() {
		println "userLikes:" + facebook.getUserLikes(siteId)

	}

	def void getFamily() {
		println "family:" + facebook.getFamily(siteId)
	}

	// success, token required
	// timeline picture, over picture, untitled picture, prifile picture
	def void getAlbums() {
		def albums = facebook.getAlbums(siteId)
		println "album:" + albums.size()
		//		albums.each {album ->
		//			println album
		//			println "-"
		//		}
	}

	// success, user
	def void accounts() {
		def accounts = facebook.getAccounts(siteId)
		println "accounts:" + accounts
	}

	// success, token required
	def void getMe() {
		println "getMe:"+facebook.getMe()
	}

	// success
	def void getUsers() {
		def user = facebook.getUser("100002495957059")
		println user
	}

	// fail, user,
	def void getFriends() {
		println "getFriends:" + facebook.getFriends()
	}

	//fail
	def void searchUsers() {
		def results = facebook.searchUsers(siteId)
		println "searchUsers:" + results
	}

	// success
	def void feed() {
		//		def feeds = facebook.getFeed(site_id, new Reading().limit(20));
		println "feed:" + facebook.getFeed()
	}
}