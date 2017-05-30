package com.samsuncnt.social.tistory

import groovy.transform.TypeChecked;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

@TypeChecked
public class TistoryJsoupTest{
	private Document get(String url, boolean source) throws IOException {
//		Document doc = Jsoup.connect(url).get();
		Document doc = Jsoup
		.connect(url)
//				.timeout(5 * 1000)
		.userAgent(
				"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
		.header("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.header("Accept-Language",
				"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
		.header("Accept-Encoding", "gzip,deflate,sdch")
		.get();
		if (source) {
			System.out.println(doc.html());
		}

		return doc;
	}

	/**
	 * 파싱불가
	 * @throws IOException
	 */
	@Test
	public void listCrawlTest() throws IOException { 
		def articleUrl = "http://www.tistory.com/category/life/fashion";
		def doc = get(articleUrl, true);
		def liElements = doc.select("div.article");
		for (Element item : liElements) {
		}

//		Assert.assertNotSame(0, liElements.size());
	}
	
	/**
	 * 파싱가능
	 */
	@Test
	def void articleTest() {
		def articleUrl = "http://dhwoo1234.tistory.com/";
		def doc = get(articleUrl, false);
		def body = doc.select("div.article").text();
		println body
	}
}
