package com.tapacross.sns.instagram.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * 키워드로 인스타그램 검색결과
 * 
 * @author cuckoo03
 *
 */
public class InstagramExplorer {
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
				.getPage("https://www.instagram.com/explore/tags/%EB%A6%B0%ED%82%A8%ED%8C%8C%ED%81%AC/");

		System.out.println(page1.asXml());

	}
}
