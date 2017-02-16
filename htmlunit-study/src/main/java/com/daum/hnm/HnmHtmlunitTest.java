package com.daum.hnm;

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
public class HnmHtmlunitTest {

	final String URL =
	// 다음 블로그 검색결과 목록에서 추출한 게시물 url
	"http://www.hnsmall.com/display/goods.do?goods_code=12026740&planclass_code=00000040&plan_code=2015116129";

	WebClient webClient;
	HtmlPage page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		webClient = HtmlUnitFactory.createDefaultWebClient();

		page = webClient
				.getPage("http://www.hnsmall.com/display/Igoodscomment.do?goods_code=12026740");

		print();
	}

	private void print() {
		System.out.println(page.getUrl());
		System.out.println(page.asXml());
	}
}
