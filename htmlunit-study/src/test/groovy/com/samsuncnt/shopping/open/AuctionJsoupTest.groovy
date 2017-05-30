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
public class AuctionJsoupTest{
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
	 * @throws IOException
	 */
	public void listCrawlTest() throws IOException { 
		def articleUrl = "http://category.gmarket.co.kr/listview/List.aspx?gdmc_cd=200000507&ecp_gdlc=&ecp_gdmc=";
		def doc = get(articleUrl, false);
		def liElements = doc.select("ul#smartClickItems li");
		for (Element item : liElements) {
			def article = item.select("div a").attr("href");
			def title = item.select("span.title").text();
			println "$article, $title"
		}

		Assert.assertNotSame(0, liElements.size());
	}
	
	/**
	 * success
	 */
	@Test
	def void articleTest() {
		def articleUrl = "http://itempage3.auction.co.kr/detailview.aspx?ItemNo=B248572471&listqs=catetab%3d1%26class%3dCorner.CategoryBest%26listorder%3d0&listtitle=%EB%B2%A0%EC%8A%A4??00&frm2=through#product_comment";
		def doc = get(articleUrl, true);
		def body = doc.select("div.article").text();
		println body
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
	def void bestListTest() {
		def count = 0  
		String articleUrl = "http://corners.auction.co.kr/corner/categorybest.aspx?catetab=1";
		def doc = get(articleUrl, false);
		def liElementsFirst = doc.select("ul[class=uxb-img first] li");
		for (Element item : liElementsFirst) {
			def article = item.select("div.img a").attr("href");
			def title = item.select("div.img a img").attr("alt");
			println "$title $article"
			if (article != "")
				count++
		}
		
		def liElements = doc.select("ul[class=uxb-img] li");
		for (Element item : liElements) {
			def article = item.select("div.img a").attr("href");
			def title = item.select("div.img a img").attr("alt");
			println "$title $article"
			if (article != "")
				count++
		}
		println "count:$count"
	} 
}
