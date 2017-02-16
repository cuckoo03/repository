package com.facebook.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * <p>
 * </p>
 * 
 * @author cuckoo03
 *
 */
public class FacebookHtmlUnit {
	final String CAFE_URL = "https://www.facebook.com/insight.co.kr/?fref=nf";
	WebClient webClient;
	HtmlPage page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		webClient = HtmlUnitFactory.createDefaultWebClient();

		page = webClient.getPage(CAFE_URL);
		print();
//		HtmlDivision div = page
//				.getFirstByXPath("//div[@id='PageHeaderPageletController_374726359324617']");
//		String referrer = div.getAttribute("data-referrer");
//		System.out.println(referrer);
//		System.out.println(referrer.substring(referrer.indexOf("_") + 1,
//				referrer.length()));
	}

	private void print() {
		System.out.println(page.getUrl());
		System.out.println(page.asXml());
	}
}
