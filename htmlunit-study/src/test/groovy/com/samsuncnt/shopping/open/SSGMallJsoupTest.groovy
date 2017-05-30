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
public class SSGMallJsoupTest{
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
	def void articleCommentTest() {
		String articleUrl = "http://diary2.gmarket.co.kr//Review/ViewPremiumReviewLayer?flag=v&prvw_no=46292663";
		def doc = get(articleUrl, false);
		def body = doc.select("div.article").text();
		println body
	}
	
	/**
	 * success
	 */
	@Test
	def void bestListTest() {
		def count = 0  
		String articleUrl = "http://shinsegaemall.ssg.com/best/bestShop.ssg?ctgId=3600054420";
		def doc = get(articleUrl, false);
		def liElements = doc.select("ul.csm_ul li.csm_li");
		for (Element item : liElements) {
			def article = item.select("div a").attr("href");
			def title = item.select("div a img").attr("alt");
			println "$title $article"
			count++
		}
		println "count:$count"
	} 
}
