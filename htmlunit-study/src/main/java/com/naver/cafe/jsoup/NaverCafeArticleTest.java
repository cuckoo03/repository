package com.naver.cafe.jsoup;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

/**
 * 게시물 필드 파싱 가능, 대신 모바일 url을 파싱해야함.pc url은 파싱 불가
 * 
 * @author cuckoo03
 *
 */
public class NaverCafeArticleTest {
	final String ARTICLE_URL = "http://m.cafe.naver.com/ArticleRead.nhn?clubid=25626825&articleid=2957";
	// final String ARTICLE_URL =
	// "http://m.cafe.naver.com/ArticleRead.nhn?clubid=25626825&articleid=2957&boardtype=null&referrerAllArticles=true&menutype=B&wCmt=true&commentCount=0&commentNew=false&rClick=false";
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	Document page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		this.page = Jsoup.connect(ARTICLE_URL).get();
//		 print(page.html());
		crawlArticle();
	}

	private void crawlArticle() {
		String siteName = page.select("span.ellip").text();
		System.out.println("siteName=" + siteName);

		String title = page.select("div.post_title > h2.tit").first().text();
		System.out.println("title=" + title);

		String writerName = page.select("div.post_title > div.info").get(0)
				.select("span.ellip").first().text();
		System.out.println("writerName=" + writerName);

		String date = page.select("div.post_title > div.info").get(1)
				.select("span[class=date font_l]").first().text();
		System.out.println("date=" + date);

		String viewCount = page.select("div.post_title > div.info").get(1)
				.select("span[class=no font_l] > em").first().text();
		System.out.println("view count=" + viewCount);

		String content = page.select("div[id=postContent]").first().text();
		System.out.println("content=" + content);
	}

	private void print(String html) {
		System.out.println(html);
	}
}
