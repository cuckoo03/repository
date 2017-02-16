package com.naver.news;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

/**
 * 
 * @author cuckoo03
 *
 */
public class NaverNewsArticleTest {
	final String ARTICLE_URL = "http://news.naver.com/main/read.nhn?mode=LSD&mid=sec&sid1=102&oid=015&aid=0003566545";
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	Document page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		this.page = Jsoup
				.connect(ARTICLE_URL)
				.timeout(10 * 1000)
				.userAgent(
						"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
				.header("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Language",
						"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
				.header("Accept-Encoding", "gzip,deflate,sdch").get();

		// print(page.html());
		crawlArticle();
	}

	private void crawlArticle() {
		// String content = page.select("div#articeBody").text();
		String content = page.select("div[id=articleBodyContents]").text();
		System.out.println("content=" + content);
	}

	private void print(String html) {
		System.out.println(html);
	}
}
