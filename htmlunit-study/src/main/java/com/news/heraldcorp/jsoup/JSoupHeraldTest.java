package com.news.heraldcorp.jsoup;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * 헤럴드경제 수집 테스트
 * 
 * @author cuckoo03
 *
 */
public class JSoupHeraldTest {
	private Document get(String url, boolean source) throws IOException {
		Document doc = Jsoup.connect(url).get();
		if (source) {
			System.out.println(doc.html());
		}

		return doc;
	}

	@Test
	public void listCrawlTest() throws IOException {
		String articleUrl = "http://biz.heraldcorp.com/list.php?ct=010000000000";
		Document doc = get(articleUrl, false);
		List<Element> liElements = doc.select("div.list ul li");
		for (Element item : liElements) {
			String article = item.select("a").attr("href");
			String title = item.select("a div div[class=list_t1 ellipsis]")
					.text();
//			System.out.println("http://biz.heraldcorp.com/" + article + ", "
//					+ title);
		}

		Assert.assertNotSame(0, liElements.size());
	}

	/**
	 * @throws IOException
	 */
	@Test
	public void crawlArticleDetailTest() throws IOException {
		String articleUrl = "http://biz.heraldcorp.com/view.php?ud=20170427001005";
		Document doc = get(articleUrl, true);

		Element contentElement = doc.select("div#content_ADTOM div#articleText").first();
		 contentElement.select("table").remove();

		String content = doc.select("div#content_ADTOM div#articleText").text();
//		System.out.println(content);

		Assert.assertNotNull(content);
	}
}
