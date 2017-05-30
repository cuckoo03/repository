package com.comm.dcinside.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 다음 블로그 게시물 수집 테스트 게시물 url에서 파싱 추출, 성공
 * 
 * @author cuckoo03
 *
 */
public class DCInsideArticleTest {
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	private Document doc;
	public int CONTENT = 0;
	int count = 0;

	public void test() throws IOException {
		String url = "http://gall.dcinside.com/board/view/?id=airforce&no=271664";

		try {
//			doc = Jsoup.connect(url).userAgent(userAgent).referrer(url)
//					.timeout(5 * 1000).get();
			doc = Jsoup.connect(url)
			.timeout(10 * 1000)
			.userAgent(
					"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
			.header("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
			.header("Accept-Language",
					"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
			.header("Accept-Encoding", "gzip,deflate,sdch").get();

			System.out.println(doc.html());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		DCInsideArticleTest test = new DCInsideArticleTest();
		test.test();
	}
}