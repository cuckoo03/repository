package com.daum.blog.jsoup;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * 다음 개인 블로그 메인 수집 테스트, 게시물 전체 목록에 접근하기 위해서는 blogid 추출이 필요.
 * @author cuckoo03
 *
 */
public class DaumBlogMainTest {
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	private Document doc;

	// 결과 url
	// http://blog.daum.net/_blog/BlogTypeMain.do?blogid=0F6hq&alllist=Y
	// http://blog.daum.net/jqka1211/7643451
	@Test
	public void test() {
//		String url = "http://blog.daum.net/brcc";
		String url ="http://agora.media.daum.net/best/rss/best?groupId=3&bbsId=all"; 
		try {
			Connection.Response res = Jsoup.connect(url).execute();
			System.out.println(res.statusCode());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(doc.html());
		
		// blogid 파싱
		Element frame = doc.select("frame[name=BlogMain]").first();
		///_blog/BlogTypeMain.do?blogid=0rtws&admin=
		System.out.println(frame.attr("src"));
	}
}