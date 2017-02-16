package com.naver.blog.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * <p>
 * 네이버 블로그 게시글 수집 테스트
 * 파싱후 전체 html 출력 실패
 * </p>
 * 
 * @author cuckoo03
 *
 */
public class NaverHtmlUnit {
	//http://blog.naver.com/otrbel54?Redirect=Log&logNo=220507421284&from=section
	final String CAFE_URL = "http://blog.naver.com/PostView.nhn?blogId=otrbel54&logNo=220507421284&redirect=Dlog&widgetTypeCall=true";
	WebClient webClient;
	HtmlPage page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		webClient = HtmlUnitFactory.createDefaultWebClient();

		page = webClient.getPage(CAFE_URL);
		print();
	}

	private void print() {
		System.out.println(page.getUrl());
		System.out.println(page.asXml());
	}
}
