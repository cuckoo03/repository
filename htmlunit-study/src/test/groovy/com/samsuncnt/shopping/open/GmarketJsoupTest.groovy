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
public class GmarketJsoupTest{
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
	 * success
	 * @throws IOException
	 */
	@Test
	public void listCrawlTest() throws IOException { 
		def articleUrl = "http://category.gmarket.co.kr/listview/List.aspx?gdmc_cd=200002669&ecp_gdlc=&ecp_gdmc=";
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
	 * fail
	 */
	@Test
	def void articleTest() {
		def articleUrl = "http://adgate.gmarket.co.kr/core/click?nextUrl=http%3A%2F%2Fitem2.gmarket.co.kr%2Fitem%2Fdetailview%2FItem.aspx%3Fgoodscode%3D200669377%26pos_shop_cd%3DSH%26pos_class_cd%3D111111111%26pos_class_kind%3DT%26search_keyword%3D&data=EF92B48DE464CA9B38DC79ACCF6787D6955E371DECC11270707CB6ED1A8AFE2C640D9908D026DEECD0EFC009AB7E29183678CE3884CA8D74D8125DF0424603759858B7BF840DC42161DA805A91B690D44675DCF6DA1A48BEDB335EF2C3F80EE0513AF40B8F8AB525C8AFA4F3921B07C3729DD9355A57A5EED2513E04FC9D956C2BAA71B5D676417EAFE388F06E1F0724E23E8E8506C4F58A5E07D413F7B701D1804E2E39289EE777415B6C19463A24F0D978F18F9466B3D523ADA8D1D57E9669BABC8968EED6C1A0DAFBBE91060FFB72208413505A2A891A18C4DCD53512313D350F85F9DD46F3785F658AA91A0897D0F6A3A0E7C7EFBBBE5D912B5E933F8CFF6A23FBDD7028499BE5448CB0ED2FF47B&referrer=&type=2&seq=26807832141";
		def doc = get(articleUrl, false);
		def body = doc.select("div.article").text();
		println body
	}
	
	/**
	 * success
	 */
	@Test
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
		String articleUrl = "http://corners.gmarket.co.kr/Bestsellers?viewType=G&groupCode=G01&subGroupCode=S002&largeCategoryCode=100000046&mediumCategoryCode=200000350";
		def doc = get(articleUrl, false);
		def liElements = doc.select("div.best-list ul li");
		for (Element item : liElements) {
			def article = item.select("div a ").attr("href");
			def title = item.select("div a img").attr("alt");
			println "$article, $title"
		}
	} 
}
