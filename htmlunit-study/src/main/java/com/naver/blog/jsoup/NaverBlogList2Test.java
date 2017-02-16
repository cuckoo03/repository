package com.naver.blog.jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * 키워드로 네이버 블로그 검색 결과 목록 파싱 테스트. 성공.
 * 
 * @author cuckoo03
 *
 */
public class NaverBlogList2Test {
	@Test
	public void test() throws UnsupportedEncodingException {
		// http://blog.naver.com/PostList.nhn?blogId=otrbel54&from=postList&categoryNo=1
		String url = "http://section.blog.naver.com/sub/SearchBlog.nhn?type=post&option.keyword="
				+ URLEncoder.encode("한글", "UTF-8")
				+ "&term=&option.startDate=&option.endDate=&option.page.currentPage="
				+ 1 + "&option.orderBy=date";
		// String url = "http://blog.naver.com/otrbel54/220507421284"; 파싱 실패
		try {
			Document doc = Jsoup.connect(url).get();
			System.out.println(doc.html());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
