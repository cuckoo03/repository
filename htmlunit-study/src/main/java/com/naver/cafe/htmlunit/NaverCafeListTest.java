package com.naver.cafe.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * <p>
 * 게시물별 url 파싱 가능 
 * </p>
 * 
 * @author cuckoo03
 *
 */
public class NaverCafeListTest {
	final String CAFE_URL = "http://section.cafe.naver.com/ArticleSearch.nhn?query=SM5&where=&x=0&y=0";
	final String M_CAFE_URL = "http://m.cafe.naver.com/SectionArticleSearch.nhn?page=1&sortBy=0&query=%20%EC%95%84%EC%9D%B4%ED%8F%B0";
	WebClient webClient;
	HtmlPage page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		webClient = HtmlUnitFactory.createDefaultWebClient();
		page = webClient.getPage(CAFE_URL);
//		print();
		@SuppressWarnings("unchecked")
		List<HtmlAnchor> list = (List<HtmlAnchor>) page.getByXPath("//ul[@id='ArticleSearchResultArea']/li/dl/dt/a");
		
		HtmlPage p1 = list.get(0).click();
		System.out.println(p1.asXml());
		System.out.println(p1.getUrl());
	}

	private void print() {
		System.out.println(page.getUrl());
		System.out.println(page.asXml());
	}
}
