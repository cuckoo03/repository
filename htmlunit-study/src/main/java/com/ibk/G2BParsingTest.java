package com.ibk;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Ignore;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * @author cuckoo03
 *
 */
public class G2BParsingTest {
	final String URL = "http://www.g2b.go.kr/pt/menu/selectSubFrame.do?framesrc=http://www.g2b.go.kr:8340/search.do?category=TGONG&kwd=%C1%DF%BC%D2%B1%E2%BE%F7%C0%BA%C7%E0";// pc
	final String ARTICLE_URL = "http://www.g2b.go.kr/pt/menu/selectSubFrame.do?framesrc=http://www.g2b.go.kr:8340/search.do?category=TGONG&kwd=%C1%DF%BC%D2%B1%E2%BE%F7%C0%BA%C7%E0";// pc
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	Document page;

	WebClient webClient;
	HtmlPage htmlPage;

	@Test
	@Ignore
	public void parseJsoupTest() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException, JSONException {
		Document doc = Jsoup.connect(ARTICLE_URL).get();
		System.out.println(doc.html());
	}

	@Test
	public void parseHtmlUnitTest() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		webClient = HtmlUnitFactory.createDefaultWebClient();
		HtmlPage htmlPage = webClient.getPage(ARTICLE_URL);
		System.out.println(htmlPage.asXml());
	}

	private void parsingArticle(String ariticleUrl) throws IOException {
		Document doc = Jsoup.connect(ariticleUrl).userAgent(userAgent)
				.referrer(ariticleUrl).timeout(5 * 1000).get();
		print(doc.html());
	}

	private void print(String html) {
		System.out.println(html);
	}
}
