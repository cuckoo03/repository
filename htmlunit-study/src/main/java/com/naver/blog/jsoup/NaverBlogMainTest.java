package com.naver.blog.jsoup;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * 네이버 개인 블로그 메인 테스트, 한 화면에 출력된 게시글 수집 가능, 네비게이션 파싱 테스트 성공 블로그미와 개인도메인 블로그는 한번 더
 * 파싱처리를 해야 한다.
 * 
 * @author cuckoo03
 *
 */
public class NaverBlogMainTest {
	@Test
	public void test() {
//		String url = "http://jh2326y.blog.me";
		// String url = "http://jiamomcake.com/";
		// String url =
		 String url = "http://12g.co.kr";
		// String url =
		// "http://blog.naver.com/PostList.nhn?from=postList&blogId=otrbel54&currentPage=1";
		try {
			Document doc = Jsoup.connect(url).get();
			System.out.println(doc.html());
			
			System.out.println(doc.select("frame[id=screenFrame]").attr("src"));

			// 컨텐트 파싱
			Element el = doc.select("div[class=post-view pcol2 _param(1)]")
					.first();
			 System.out.println(el.text());

			// 네비게이션 파싱
			List<Element> naviAnchors = doc
					.select("table[class=page-navigation] > tbody > tr > td > a");
			System.out.println(naviAnchors);
			System.out.println(naviAnchors.size());

			String page2Url = "http://blog.naver.com/PostList.nhn?from=postList&blogId=otrbel54&currentPage=2";
			doc = Jsoup.connect(page2Url).get();
			System.out.println(doc.html());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
