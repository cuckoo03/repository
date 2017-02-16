package com.naver.cafe.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * <p>
 * 게시물 필드 파싱 가능, 대신 모바일 url을 파싱해야함.pc url은 파싱 불가
 * </p>
 * 
 * @author cuckoo03
 *
 */
public class NaverCafeArticleTest {
	final String ARTICLE_URL = "http://m.cafe.naver.com/ArticleRead.nhn?clubid=25626825&articleid=2957&boardtype=null&referrerAllArticles=true&menutype=B&wCmt=true&commentCount=0&commentNew=false&rClick=false";
	WebClient webClient;
	HtmlPage page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		webClient = HtmlUnitFactory.createDefaultWebClient();
		page = webClient.getPage(ARTICLE_URL);
		print();
	}

	private void print() {
		System.out.println(page.getUrl());
		System.out.println(page.asXml());
	}
}
