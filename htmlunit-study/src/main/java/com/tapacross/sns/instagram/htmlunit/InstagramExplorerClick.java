package com.tapacross.sns.instagram.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * 키워드로 검색한 결과에서 게시물 클릭한 상세화면
 * 
 * @author cuckoo03
 *
 */
public class InstagramExplorerClick {
	WebClient webClient;
	HtmlPage page;

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		webClient = HtmlUnitFactory.createDefaultWebClient();

		long start = System.currentTimeMillis();
		final HtmlPage page1 = webClient
				.getPage("https://instagram.com/p/5OJKXbQImu/?tagged=%EB%A6%B0%ED%82%A8%ED%8C%8C%ED%81%AC");


		System.out.println(page1.asXml());
		System.out.println(System.currentTimeMillis() - start);
		// HTML 에서 form 객체를 가져온다.
		List<HtmlListItem> list = (List<HtmlListItem>) page1
				.getByXPath("//li[@class='-cx-PRIVATE-PostInfo__comment']");
		System.out.println(list.size());

		for (HtmlListItem li : list) {
			// print userid
			List<HtmlElement> anchors = li.getElementsByTagName("a");
			for (HtmlElement anchor : anchors) {
				System.out.println(anchor.getAttribute("href"));
				break;
			}
			
			// print comment
			List<HtmlElement> spans = li.getElementsByTagName("span");
			for (HtmlElement span : spans) {
				System.out.println(span.asText());
				break;
			}
			System.out.println("--------");
		}
	}
}
