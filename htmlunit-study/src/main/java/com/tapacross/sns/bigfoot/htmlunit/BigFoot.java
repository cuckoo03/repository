package com.tapacross.sns.bigfoot.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * 키워드로 인스타그램 검색결과
 * 
 * @author cuckoo03
 *
 */
public class BigFoot {
	WebClient webClient;
	HtmlPage page;

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		webClient = HtmlUnitFactory.createDefaultWebClient();
//		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//		webClient.getOptions().setThrowExceptionOnScriptError(false);
		
		HtmlPage page1 = webClient
				.getPage("http://bigfoot9.com/overview?cc=KR&cat=AUTOMOBILES+AND+PARTS&pgo=pis&pto=pis");

		System.out.println(page1.asXml());

	}
}
