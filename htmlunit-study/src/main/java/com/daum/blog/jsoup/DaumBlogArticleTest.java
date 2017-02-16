package com.daum.blog.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 다음 블로그 게시물 수집 테스트 게시물 url에서 파싱 추출, 성공
 * 
 * @author cuckoo03
 *
 */
public class DaumBlogArticleTest {
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	private Document doc;
	public int CONTENT = 0;
	int count = 0;

	@SuppressWarnings("unused")
	public void test() throws IOException {
		// String url =
		// "http://blog.daum.net/_blog/BlogTypeView.do?blogid=0F6hq&amp;articleno=7643452&amp;_bloghome_menu=recenttext";
		// String url = "http://blog.daum.net/brcc/736"; //735";
		String url = "http://blog.daum.net/act4ksj/13765489";
		
		try {
			doc = Jsoup.connect(url).userAgent(userAgent).referrer(url)
					.timeout(5 * 1000).get();

			// http://blog.daum.net/_blog/BlogTypeView.do?blogid=0rtws&articleno=724&admin=
			// //개별 블로그 게시글 프레임
			url = "http://blog.daum.net" + doc.select("frame").attr("src");
			String SCREEN_NAME = doc.select("title").text();

			doc = Jsoup.connect(url).userAgent(userAgent).referrer(url)
					.timeout(5 * 1000).get();
			// System.out.println(doc.html());

			// http://blog.daum.net/_blog/hdn/ArticleContentsView.do?blogid=0rtws&articleno=724&looping=0&longOpen=
			url = "http://blog.daum.net" + doc.select("iframe").attr("src");

			doc = Jsoup.connect(url).userAgent(userAgent).referrer(url)
					.timeout(5 * 1000).get();

			System.out.println("parse:" + count++);
			// System.out.println(doc.html());
			CONTENT = doc.select("div#cContent").text().hashCode();
			System.out.println(CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		DaumBlogArticleTest test = new DaumBlogArticleTest();
		int prev = 0;
		int current = 0;
		while (true) {
			test.test();
			if (prev == 0) {
				current = test.CONTENT;
				prev = test.CONTENT;
			}
			current = test.CONTENT;
			if (prev != current) {
				System.err.println("hashcode different");
				System.exit(1);
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}