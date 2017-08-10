package com.insight

import groovy.transform.TypeChecked

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.junit.Ignore;
import org.junit.Test

@TypeChecked
class InsightJsoupTest {
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"
	private Document doc
	public int CONTENT = 0
	int count = 0

	@Test
	@Ignore
	public void test() throws IOException {
		String url = "http://www.insight.co.kr/more/?mc=003"

		try {
			//			doc = Jsoup.connect(url).userAgent(userAgent).referrer(url)
			//					.timeout(5 * 1000).get();
			doc = Jsoup.connect(url)
					.timeout(10 * 1000)
					.userAgent(
					"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
					.header("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
					.header("Accept-Language",
					"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
					.header("Accept-Encoding", "gzip,deflate,sdch").get()

			System.out.println(doc.html())
			
			def anchors = doc.select("ul > li > div.container-post-title > a")
				anchors.each { Element it ->
					println it.attr("href")
				}

		} catch (Exception e) {
			e.printStackTrace()
		}
	}
	
	@Test
	public void testArticle() throws IOException {
		String url = "http://www.insight.co.kr/newsRead.php?ArtNo=114236"

		try {
			doc = Jsoup.connect(url)
					.timeout(10 * 1000)
					.userAgent(
					"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
					.header("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
					.header("Accept-Language",
					"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
					.header("Accept-Encoding", "gzip,deflate,sdch").get()

			System.out.println(doc.html())
			
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
}
