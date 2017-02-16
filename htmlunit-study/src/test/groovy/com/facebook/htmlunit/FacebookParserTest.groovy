package com.facebook.htmlunit

import groovy.transform.TypeChecked

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.junit.Ignore
import org.junit.Test
import org.openqa.selenium.WebDriver

import com.ExtendedHtmlUnitDriver

/**
 * 페이스북 게시물 수집 테스트
 * 모바일 페이지는 파싱하는데 필요한 일부 핃드(게시물 작성시간)가 없어 사용이 불가하다.
 **/
@TypeChecked
class FacebookParserTest {

	@Test
	public void parseArticlesTest() {
		def driver = createHtmlUnitDriver()
		def keyword = "아이폰"
		final def URL = "https://www.facebook.com/hashtag/" + keyword
		final def M_URL = "https://m.facebook.com/hashtag/" + keyword
		driver.get(URL)

		//		println driver.getPageSource()

		parseArticles(driver)
	}

	/**
	 * 게시물url을 기준으로 게시물을 파싱한다
	 * 게시물의 엘리멘트 위치를 찾기 힘들어 작업 중지.
	 */
	@Test
	@Ignore
	public void parseArticleTest() {
		def driver = createHtmlUnitDriver()
		def start = System.currentTimeMillis()
		
		final def URL = "https://www.facebook.com/404968689618870/posts/1004395389676194"
		driver.get(URL)

		println "Elased:" + (System.currentTimeMillis() - start) / 1000
		def doc = Jsoup.parse(driver.getPageSource())
		
		parseHiddenElement2(doc);
	}

	@Test
	@Ignore
	def  void parseProfileTest() {
		def driver = createHtmlUnitDriver()
		def start = System.currentTimeMillis()
		final def M_URL = "https://m.facebook.com/wikitree.page/"
		driver.get(M_URL)

		println driver.getPageSource()

		println "Elased:" + (System.currentTimeMillis() - start) / 1000

		parseProfiles(driver)
	}

	private void parseProfiles(WebDriver driver) {
		def doc = Jsoup.parse(driver.getPageSource())

		def siteId = driver.getCurrentUrl().
				replace("https://m.facebook.com/", "").replace("/", "")
		println "siteId:$siteId"

		// /login/?next=https%3A%2F%2Fm.facebook.com%2Fwikitree.page%2F&page_id=118955304823840&context_type=like&refid=17
		def tagProperties = doc.select("div > div > table > tbody > tr > td > a").
				get(0).attr("href").replace("/login/?", "").split("&")
				
				
		// 좋아요 태그의 값에서 siteCode를 추출한다.
		def siteCode = doc.select("div > div > table > tbody > tr > td > a").
				get(1).attr("href").
				replace("/messages/thread/", "").replace("/?refid=17", "")
		println "siteCode:$siteCode"

		def bio = doc.select("span[itemprop=name]").text()
		println "bio:$bio"

		def like = doc.select("a > table > tbody > tr > td > div > div").
				get(0).text().replace("명이 좋아합니다", "").replace(",", "")
		println "like:$like"
	}

	private void parseArticles(WebDriver driver) {
		def doc = Jsoup.parse(driver.getPageSource())
		def hiddenElements = doc.select("div.hidden_elem > code")
		println "hidden:" + hiddenElements.size()
		hiddenElements.each { article ->
			def elementDoc =
					Jsoup.parse(article.html().replace("<!--", "").replace("-->", ""))
			parseHiddenElement(elementDoc)
		}
	}

	private void parseHiddenElement(Document doc) {
		//		println doc.html()
		def articles = doc.select("html > body > div > div > div > div")
		println "articles:" + articles.size()
		articles.each {article ->  parseArticle(article)  }
	}
	
	private void parseHiddenElement2(Document doc) {
//		println doc.html()
		def articles = doc.select("html > body > div > div > div > div")
		println "articles:" + articles.size()
		articles.each {article ->  parseArticle2(article)  }
	}

	private void parseArticle(Element el) {
		println "**************************"
		// 인스타그램 게시물일 경우 사이트명이 빈문자열 일 수 있다
		def siteName = el.select("div > h5 > span > span > a").text()
		println "siteName:$siteName"
		def createDate = el.select("abbr").attr("title")
		println "createDate:$createDate"
		def createUTime = el.select("abbr").attr("data-utime")
		println "createTime:$createUTime"

		def body = el.select("div > p").text()
		println "body:$body"

		def siteUrl = el.select("div > div > a").attr("href").replace("?ref=nf", "")
		println "siteUrl:$siteUrl"

		def articleUrl = "https://www.facebook.com" + el.select("div > span > span > a").attr("href")
		println "articleUrl:$articleUrl"
	}
	
	private void parseArticle2(Element el) {
		println "**************************"
		// 인스타그램 게시물일 경우 사이트명이 빈문자열 일 수 있다
		def a = el.select("div > h5 > span > span > span > a")
		if (a.size() <= 0)
			return
			
		def siteName = el.select("div > h5 > span > span > span > a").get(0).text()
		println "siteName:$siteName"
		
		def b = el.select("span > a > abbr")
		if (b.size() <= 0)
			return
			
		def createDate = el.select("span > a > abbr").get(1).attr("title")
		println "createDate:$createDate"
		
		def c = el.select("span > a > abbr")
		def createUTime = el.select("span > a > abbr").get(1).attr("data-utime")
		println "createTime:$createUTime"

		def d = el.select("div[role=article] > div > div")
		def body = el.select("div[role=article] > div > div").get(1).text()
		println "body:$body"

		def siteUrl = el.select("div > div > a").attr("href").replace("?ref=nf", "")
		println "siteUrl:$siteUrl"

		def articleUrl = "https://www.facebook.com" + el.select("div > span > span > a").attr("href")
		println "articleUrl:$articleUrl"
	}
	
	private WebDriver createHtmlUnitDriver() {
		ExtendedHtmlUnitDriver driver = new ExtendedHtmlUnitDriver(true)
		driver.setJavascriptEnabled(true)
		driver.setThrowExceptionOnScriptError(false)
		driver.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		driver.setHeader("Accept-Language",
				"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
		return driver
	}
}
