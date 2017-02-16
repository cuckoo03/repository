package com.tapacross.sns.instagram.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

public class InstagramLogin {
	WebClient webClient;
	HtmlPage page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		webClient = HtmlUnitFactory.createDefaultWebClient();

		// java.util.logging.Logger.getLogger("com.gargoylesoftware")
		// .setLevel(Level.OFF);

		final HtmlPage page1 = webClient
				.getPage("https://instagram.com/accounts/login/");

		System.out.println(page1.asXml());

	}
}
