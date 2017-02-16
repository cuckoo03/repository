package com.nate.keyword.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Ignore;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

public class NateKeywordTest {
	String URL = "http://search.daum.net/nate?nil_suggest=btn&w=tot&DA=SBCO&q=";
	WebClient webClient;
	HtmlPage page;

	@Test
	@Ignore
	public void test() {
		try {
			Document doc = Jsoup.connect(URL).get();
			System.out.println(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testHttpUnit() {
		webClient = HtmlUnitFactory.createDefaultWebClient();

		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(
				Level.OFF);

		try {
			page = (HtmlPage) webClient.getPage(URL);
			System.out.println(page.asXml());
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
