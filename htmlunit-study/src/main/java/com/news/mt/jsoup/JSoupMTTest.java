package com.news.mt.jsoup;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * 머니투데이 수집 테스트
 * @author cuckoo03
 *
 */
public class JSoupMTTest {
	private Document get(String url, boolean source) throws IOException {
		Document doc = Jsoup.connect(url).get();
		if (source) {
			System.out.println(doc.html());
		}
		
		return doc;
	}
	
	@Test
	public void commentCrawlTest() throws IOException {
		String articleUrl = "http://news.mt.co.kr/newsList.html?pDepth1=tech&pDepth2=Ttotal";
		Document doc = get(articleUrl, false);
		List<Element> liElements = doc.select("ul[class=conlist_p1 mgt30] li");
		for (Element item : liElements) {
			String article = item.select("a").attr("href");
			System.out.println(article);
		}
		
		Assert.assertNotSame(0, liElements.size());
	}
	
	/**
	 * @throws IOException
	 */
	@Test
	public void crawlArticleDetailTest() throws IOException {
		String articleUrl = "http://news.mt.co.kr/mtview.php?no=2017042516513634156&type=2&sec=tech&pDepth2=Ttotal";
		Document doc = get(articleUrl, false);
		
		String content = doc.select("div.view_text").text();
		System.out.println(content);
		
		Assert.assertNotNull(content);
	}
}
