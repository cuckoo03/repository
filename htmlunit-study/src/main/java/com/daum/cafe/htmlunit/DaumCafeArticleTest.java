package com.daum.cafe.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * <p>
 * 게시물 필드 정보 파싱 가능
 * </p>
 * 
 * @author cuckoo03
 *
 */
public class DaumCafeArticleTest {
	final String ARTICLE_URL = "http://cafe.daum.net/_c21_/bbs_search_read?grpid=Uzlo&fldid=LnOm&datanum=1522364&q=%BE%C6%C0%CC%C6%F9&_referer=V7kfJwkeLEGMZxGlgqZEmagITtKVchRIEjxRA9eZWR1EFD1XRmFC2A00";
	final String ARTICLE_URL2 = "http://cafe.daum.net/_c21_/bbs_search_read?grpid=aVeZ&fldid=9Zdf&datanum=351989&q=%BE%C6%C0%CC%C6%F9&_referer=V7kfJwkeLEE_BcNpTVULwWlKmNlFJ16Vpz26BMPo_oKSsk.VV2dhpx7r3BnRdAnb";
	final String ARTICLE_URL3 = "http://cafe.daum.net/_c21_/bbs_search_read?grpid=1IHuH&fldid=Lovh&datanum=1446692&q=%BE%C6%C0%CC%C6%F9&_referer=V7kfJwkeLEE_BcNpTVULwWlKmNlFJ16Vpz26BMPo_oKSsk.VV2dhpx7r3BnRdAnb";
	WebClient webClient;
	HtmlPage page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		webClient = HtmlUnitFactory.createDefaultWebClient();
		page = webClient.getPage(ARTICLE_URL);
		// print();
		parsingArticle(Jsoup.parse(page.asXml()));
	}

	private void parsingArticle(Document doc) {
		String siteName = doc.select("meta[property=og:site_name]").attr(
				"content");
		System.out.println("siteName=" + siteName);

		String title = null;
		// mobile에서 작성할 경우
		if (doc.select("div.subject > span.b > b").first() == null) {
			title = doc.select("div.subject > span.b").first().text();
		} else { // 그외 피시에서 작성할 경우
			title = doc.select("div.subject > span.b > b").first().text();
		}
		System.out.println("title=" + title);

		String writerName = doc.select("div.article_writer > a").first().text();
		System.out.println("writerName=" + writerName);

		String searchCount = doc.select("div.article_writer > span.p11").get(0)
				.text();
		System.out.println("searchCount=" + searchCount);

		String like = doc.select("div.article_writer > span.p11").get(1).text();
		System.out.println("like=" + like);

		String date = doc.select("div.article_writer > span[class=p11 ls0]")
				.first().text();
		System.out.println("date=" + date);

		// System.out.println(doc.select("table.protectTable").first().text());
		Document contentEl = Jsoup.parse(doc.select("table.protectTable")
				.first().html());

		String content = contentEl.text();
		Document contentDoc = Jsoup.parse(content);
		// System.out.println(contentDoc.html());
		System.out.println("content=" + contentDoc.text());
	}

	private void print() {
		System.out.println(page.getUrl());
		System.out.println(page.asXml());
	}
}
