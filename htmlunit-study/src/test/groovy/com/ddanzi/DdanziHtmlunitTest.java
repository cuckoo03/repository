package com.ddanzi;

import java.io.IOException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

public class DdanziHtmlunitTest {
	WebClient webClient;
	HtmlPage page;
	@Test
	public void test() {
		String url = "http://www.ddanzi.com/free";
		try {
			webClient = HtmlUnitFactory.createDefaultWebClient();
			
			page = (HtmlPage) webClient.getPage(url);
			System.out.println(page.asXml());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
