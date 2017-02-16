package com.daum.blog.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * <p>
 * 다음 개인 블로그 게시글 수집 테스트
 * </p>
 * 
 * @author cuckoo03
 *
 */
public class DaumBlogArticleTest {

	final String URL =
	// 다음 블로그 검색결과 목록에서 추출한 게시물 url
	"http://blog.daum.net/_blog/hdn/ArticleContentsView.do?blogid=0rtws&articleno=724&looping=0&longOpen=";

	WebClient webClient;
	HtmlPage page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		webClient = HtmlUnitFactory.createDefaultWebClient();

		page = webClient.getPage("http://blog.daum.net/_blog/hdn/ArticleContentsView.do?blogid=0p4Sd&articleno=402&looping=0&longOpen=");

		print();
	}

	private void print() {
		System.out.println(page.getUrl());
		System.out.println(page.asXml());
	}
}
