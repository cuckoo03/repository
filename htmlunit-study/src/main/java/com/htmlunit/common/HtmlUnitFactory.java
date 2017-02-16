package com.htmlunit.common;

import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

public class HtmlUnitFactory {
	public static WebClient createHtmlUnit(Map<String, String> paramMap) {
		return null;
	}
	
	public static WebClient createDefaultWebClient() {
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.addRequestHeader("Accept-Language",
				"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4, value");
		
		return webClient;
	}
}

