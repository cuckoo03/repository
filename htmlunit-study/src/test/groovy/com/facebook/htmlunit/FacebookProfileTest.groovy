package com.facebook.htmlunit

import groovy.transform.TypeChecked

import java.util.logging.Level

import org.jsoup.Jsoup;
import org.junit.Test

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCode;
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput
import com.gargoylesoftware.htmlunit.html.HtmlTextInput
import com.htmlunit.common.HtmlUnitFactory;

@TypeChecked
class FacebookProfileTest {
	def String URL = "https://www.facebook.com/"
	WebClient webClient
	HtmlPage page
	@Test
	public void loginTest() {
		webClient = HtmlUnitFactory.createDefaultWebClient();

		java.util.logging.Logger.getLogger("com.gargoylesoftware")
				.setLevel(Level.OFF);

		page = (HtmlPage) webClient.getPage(URL)

		def email = (HtmlTextInput) page.getFirstByXPath("//input[@id='email']")
		email.setValueAttribute("facebook@tapacross.co.kr")

		HtmlPasswordInput pw = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='pass']")
		pw.setValueAttribute("raise_me_up*5")

		HtmlSubmitInput submit = (HtmlSubmitInput) page.getFirstByXPath("//input[@id='u_0_y']")
		page = (HtmlPage) submit.click()

		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/about")
		
//				moveEducation()
//				moveLiving()
//				moveContactInfo()
//				moveRelationship()
//				moveBio()
//				moveYearOverview()

		//		parseBio()
		//		parseEducation()
		//		parseRelationship()
		//		parseContactInfo()
		//		parseLikesMovies()
		//		parseFriendsList()
		//		parseFavoriteMusics()
		//		parseFavoriteSports()
		//		parseFavoriteMovies()
		//		parseFavoriteTV()
		//		parsePhotos()
		//		parseTimeline()

//				moveLikesSportsTeams()
				moveLikesSportsAthletes()
				print()
//				moveLikesMusics()
//				moveLikesMovies()
//				moveLikesTV()
//				moveLikesBook()

		//		moveFriendsList()
		//		moveFavoriteMovies()
		//		moveFavoriteTV()
		//		movePhotos()
	}

	def void parseTimeline() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/tapa.man.94")
	}

	def void moveLiving() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/about?section=living&pnref=about")
	}

	def void moveLikesBook() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/books_favorite")
	}

	def void moveLikesTV() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/video_tv_shows_favorite")
	}

	def void moveYearOverview() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/about?section=year-overviews&pnref=about")
	}

	def void moveRelationship() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/about?section=relationship&pnref=about")
	}

	def void parseRelationship() {
		print()

		def codes = (List<HtmlCode>) page.getByXPath("//code[@class='hidden_elem']")
		println codes.size()
		for (int i = 0; i < codes.size(); i++) {
			if (i != 4)//4,5
				continue

			def str = codes.get(i).asXml().replace("<!--", "").replace("-->", "")
			def document = Jsoup.parse(str)
			def links = document.select("li > div > div > div > div > div > div > a")
			links.each {link ->
				println link.text()
			}
		}
	}

	def void moveEducation() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/about?section=education&pnref=about")
	}

	def void parseEducation() {
		print()

		def codes = (List<HtmlCode>) page.getByXPath("//code[@class='hidden_elem']")
		println codes.size()
		for (int i = 0; i < codes.size(); i++) {
			if (i != 4)
				continue

			def str = codes.get(i).asXml().replace("<!--", "").replace("-->", "")
			def document = Jsoup.parse(str)
			def links = document.select("li > div > div > div > div > div > div > a")
			links.each {link ->
				println link.text()
			}
		}
	}

	def void moveLikesMovies() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/video_movies_favorite")
	}

	def void parseLikesMovies() {
		def codes = (List<HtmlCode>) page.getByXPath("//code[@class='hidden_elem']")
		println ""

		println codes.size()
		for (int i = 0; i < codes.size(); i++) {
			if (i != 6)
				continue

			def str = codes.get(i).asXml().replace("<!--", "").replace("-->", "")
			def document = Jsoup.parse(str)
			def links = document.select("li > div > div > div > div > div > div > a")
			links.each {link ->
				println link.text()
			}
		}
	}

	def void movePhotos() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/photos")
		print()
	}

	def void parsePhotos() {
		def codes = (List<HtmlCode>) page.getByXPath("//code[@class='hidden_elem']")
		println ""

		println codes.size()
		for (int i = 0; i < codes.size(); i++) {
			if (i != 2)
				continue

			def str = codes.get(i).asXml().replace("<!--", "").replace("-->", "")
			def document = Jsoup.parse(str)
			println document
		}
	}

	def void moveFavoriteTV() {
	}

	def void parseFavoriteTV() {
	}

	def void moveFavoriteMovies() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/video_movies_watch")
		print()
	}

	def void parseFavoriteMovies() {
		def codes = (List<HtmlCode>) page.getByXPath("//code[@class='hidden_elem']")
		println ""

		println codes.size()
		for (int i = 0; i < codes.size(); i++) {
			if (i != 6)
				continue

			def str = codes.get(i).asXml().replace("<!--", "").replace("-->", "")
			def document = Jsoup.parse(str)
			def links = document.select("code.hidden_elem ul li div a")
			// url 중복 제거 필요
			links.each { link ->
				println "link:" + link.attr("href")
			}
		}
	}

	def void moveLikesSportsAthletes() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/sports_athletes")
	}

	def void moveLikesSportsTeams() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/sports_teams")
	}

	def void parseFavoriteSports() {
		def codes = (List<HtmlCode>) page.getByXPath("//code[@class='hidden_elem']")
		println ""

		println codes.size()
		for (int i = 0; i < codes.size(); i++) {
			if (i != 6)
				continue

			def str = codes.get(i).asXml().replace("<!--", "").replace("-->", "")
			def document = Jsoup.parse(str)
			def links = document.select("code.hidden_elem ul li div > a")
			// 중복 url 제거 필요
			links.each {link ->
				println "link:" + link.attr("href")
			}
		}
	}

	def void parseFavoriteMusics() {
		def codes = (List<HtmlCode>) page.getByXPath("//code[@class='hidden_elem']")
		println ""

		println codes.size()
		for (int i = 0; i < codes.size(); i++) {
			if (i != 6)
				continue

			def str = codes.get(i).asXml().replace("<!--", "").replace("-->", "")
			def document = Jsoup.parse(str)
			println document
			def links = document.select("code.hidden_elem ul li div div a")
			links.each { it ->
				println "link:" + it.attr("href")
			}
		}
	}

	def void moveLikesMusics() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/music")
	}

	def void parseFriendsList() {
		def codes = (List<HtmlCode>) page.getByXPath("//code[@class='hidden_elem']")
		println ""

		println codes.size()
		for (int i = 0; i < codes.size(); i++) {
			if (i != 2)
				continue

			def str = codes.get(i).asXml().replace("<!--", "").replace("-->", "")
			def document = Jsoup.parse(str)
			def links = document.select("div.uiProfileBlockContent div div div a")
			// print friends url
			links.each {doc ->
				println doc.select("a").attr("href")
			}
		}
	}

	def void moveFriendsList() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/friends")
		print()
	}

	def void parseBio() {
		print()

		def codes = (List<HtmlCode>) page.getByXPath("//code[@class='hidden_elem']")
		println ""

		println codes.size()
		for (int i = 0; i < codes.size(); i++) {
			def str = codes.get(i).asXml().replace("<!--", "").replace("-->", "")
			println str
			def document = Jsoup.parse(str)
			println document
		}
	}

	def void moveBio() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/about?section=bio&pnref=about")
	}

	def void parseContactInfo() {
		print()

		def code = page.getFirstByXPath("//code[@class='hidden_elem']")
		def codes = (List<HtmlCode>) page.getByXPath("//code[@class='hidden_elem']")
		println ""

		println codes.size()
		for (int i = 0; i < codes.size(); i++) {
			if (i != 4)
				continue

			def str = codes.get(i).asXml().replace("<!--", "").replace("-->", "")
			def document = Jsoup.parse(str)
			println document

			def phone = document.select("ul li span[dir=ltr]").text()
			def address = document.select("div span span ul li").text()
			def website = document.select("div span ul li a.uiLinkDark").attr("href")
			def email = document.select("div div div div span ul li a span").text()
			if (phone != "") {
				println "휴대폰:$phone"
				println "주소:$address"
				println "website:$website"
				email.split(" ").each {it ->
					if (((String)it).contains("@"))
						println "email:$it"
				}
			}
		}
	}


	def void moveContactInfo() {
		page = (HtmlPage) webClient.getPage("https://www.facebook.com/jaekyun.ko.7/about?section=contact-info&pnref=about")
	}

	def void print() {
		println page.asXml()
	}
}
