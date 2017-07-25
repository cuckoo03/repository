package com.comm.motline.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * 수집가능
 * @author cuckoo03
 *
 */
public class MotLineArticleTest {
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	private Document doc;
	public int CONTENT = 0;
	int count = 0;

	@Test
	public void listTest() throws IOException {
		String url = "http://www.motline.com/index.php?d=1&mid=free";

		try {
			doc = Jsoup.connect(url)
			.timeout(10 * 1000)
			.userAgent(
					"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
			.header("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
			.header("Accept-Language",
					"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
			.header("Accept-Encoding", "deflate,sdch").get();

			Elements elements = doc.select("div.board_list > table tr").not("tr.notice");
			for (Element e : elements) {
				String href = e.select("td.title > a").attr("href");
				System.out.println(href);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void list2Test() throws IOException {
		String url = "http://www.motline.com/index.php?d=1&mid=diy";

		try {
			doc = Jsoup.connect(url)
			.timeout(10 * 1000)
			.userAgent(
					"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
			.header("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
			.header("Accept-Language",
					"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
			.header("Accept-Encoding", "deflate,sdch").get();

//			System.out.println(doc.html());
			Elements elements = doc.select("div.board_list > table tr").not("tr.notice");
			for (Element e : elements) {
				String href = e.select("td.title > a").attr("href");
				System.out.println(href);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void articleTest() throws IOException {
		String url = "http://www.motline.com//index.php?d=1&mid=diy&document_srl=314817";

		try {
			doc = Jsoup.connect(url)
			.timeout(10 * 1000)
			.userAgent(
					"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
			.header("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
			.header("Accept-Language",
					"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
			.header("Accept-Encoding", "deflate,sdch").get();

			System.out.println(doc.select("div.read_body").text());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}