package com.news.newsis.jsoup;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 뉴시스 수집 테스트
 * 
 * @author cuckoo03
 *
 */
public class JSoupNewsisTest {
	private Document get(String url, boolean source) throws IOException {
		Document doc = Jsoup.connect(url).get();
		if (source) {
			System.out.println(doc.html());
		}

		return doc;
	}

	@Test
	public void listCrawlTest() throws IOException {
		String articleUrl = "http://www.newsis.com/spo/list/?cid=10500&scid=10501&page=1";
		Document doc = get(articleUrl, false);
		List<Element> liElements = doc.select("ul[class=group] li.p1_bundle");
		for (Element item : liElements) {
			String article = item.select("a").attr("href");
			String title = item.select("a").text();
			System.out
					.println("http://www.newsis.com" + article + ", " + title);
		}

		Assert.assertNotSame(0, liElements.size());
	}

	/**
	 * @throws IOException
	 */
	@Test
	public void crawlArticleDetailTest() throws IOException {
		String articleUrl = "http://www.newsis.com/view/?id=NISX20170426_0014857085&cid=10506";
		Document doc = get(articleUrl, true);

		Element contentElement = doc.select("div.view_text > div#textBody").first();
		contentElement.select("div.view_text").remove();
		
		String content = doc.select("div.view_text > div#textBody").text();
//		System.out.println(content);

		Assert.assertNotNull(content);
	}
}
