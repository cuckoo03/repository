package com.samsuncnt.shopping.open

import groovy.transform.TypeChecked;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

@TypeChecked
public class SSGEmartJsoupTest{
	private Document get(String url, boolean source) throws IOException {
//		Document doc = Jsoup.connect(url).get();
		Document doc = Jsoup
		.connect(url)
//				.timeout(5 * 1000)
		.userAgent(
				"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
		.header("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.header("Accept-Language",
				"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
		.header("Accept-Encoding", "gzip,deflate,sdch")
		.get();
		if (source) {
			System.out.println(doc.html());
		}

		return doc;
	}

	/**
	 */
	@Test
	def void articleTest() {
		String articleUrl = "http://department.ssg.com/item/itemView.ssg?itemId=1000020721759&siteNo=6009&salestrNo=1007";
		def doc = get(articleUrl, false);
	}
	
	/**
	 */
	@Test
	def void articleCommentTest() {
		String articleUrl = "http://department.ssg.com/item/ajaxItemCommentList.ssg?itemId=1000020721759&siteNo=6009&filterCol=10&sortCol=01&page=1&pageSize=10";
		def doc = get(articleUrl, false);
		def elements = doc.select("mbr-login-id");
		elements.each { Element it ->
			println it.text()
		}
	}
	
	/**
	 * success
	 */
	@Test
	def void bestListTest() {
		def count = 0  
		String articleUrl = "http://emart.ssg.com/best/sub.ssg?categoryZoneType=life&selectedCategoryLevel=L&selectedLargeCategoryId=6000014364&selectedMiddleCategoryId=";
		def doc = get(articleUrl, true);
		def liElements = doc.select("ul li[class=open_price star_on store_on comment_on]");
		for (Element item : liElements) {
			def article = item.select("span.thumb > a").attr("href");
			def title = item.select("span.thumb > a > img").attr("alt");
			println "$title $article"
			count++
		}
		println "count:$count"
	} 
}
