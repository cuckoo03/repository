package com.news.joins.jsoup;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 중앙일보 수집 테스트
 * @author cuckoo03
 *
 */
public class JSoupJoinsTest {
	private Document get(String url, boolean source) throws IOException {
		Document doc = Jsoup.connect(url).get();
		if (source) {
			System.out.println(doc.html());
		}
		
		return doc;
	}
	
	@Test
	public void commentCrawlTest() throws IOException {
		String articleUrl = "http://news.joins.com/money/economy/list?cloc=joongang|section|subsection";
		Document doc = get(articleUrl, false);
		List<Element> liElements = doc.select("div.list_basic ul li");
		for (Element item : liElements) {
			String article = item.select("span a ").attr("href");
			System.out.println("http://news.joins.com" + article);
		}
		
		Assert.assertNotSame(0, liElements.size());
	}
	
	/**
	 * @throws IOException
	 */
	@Test
	public void crawlArticleDetailTest() throws IOException {
		String articleUrl = "http://news.joins.com/article/21511088";
		Document doc = get(articleUrl, false);
		
		String content = doc.select("div#article_body").text();
		System.out.println(content);
		
		Assert.assertNotNull(content);
	}
}
