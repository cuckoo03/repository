package com.daum.hnm;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * 다음 블로그 게시물 수집 테스트 게시물 url에서 파싱 추출, 성공
 * 
 * @author cuckoo03
 *
 */
public class HnmJsoupTest {
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	private Document doc;
	public int CONTENT = 0;
	int count = 0;

	@Test
	public void test() throws IOException {
		// String url =
//		String url = "http://www.hnsmall.com/display/goods.do?goods_code=11314925";// 게시물 url
//		String url = "http://www.hnsmall.com/display/Igoodscomment.do?goods_code=12438898";
		// 후기 게시물 url
		String url ="http://www.hnsmall.com/display/Igoodscomment.do?currentPage=1&rowsPerPage=10&goods_code=11314925&order_type=1&search_type=&searchSex=&searchAgeArea=&searchOrderInfo=&goto=Y&bundleYn=&bestCount=15";

		try {
			doc = Jsoup.connect(url).userAgent(userAgent).timeout(5 * 1000)
					.get();

			System.out.println(doc.html());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}