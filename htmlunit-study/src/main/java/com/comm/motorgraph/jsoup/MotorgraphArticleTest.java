package com.comm.motorgraph.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * 모터그래프 수집가능
 * @author cuckoo03
 *
 */
public class MotorgraphArticleTest {
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	private Document doc;
	public int CONTENT = 0;
	int count = 0;

	public void listTest() throws IOException {
		String url = "http://www.motorgraph.com/news/articleList.html?view_type=sm";

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
			Elements anchors = doc.select("td.ArtList_Title > a");
			for (Element e : anchors) {
				System.out.println(e.attr("href"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void list2Test() throws IOException {
		String url = "https://c.motorgraph.com/free";

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
			Elements elements = doc.select("table[class=bd_lst bd_tb_lst bd_tb] tr").not("tr.notice");
			for (Element e : elements) {
				System.out.println(e.select("td.title > a").attr("href"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void articleTest() throws IOException {
		String url = "http://www.motorgraph.com/news/articleView.html?idxno=12593";

		try {
			doc = Jsoup.connect(url)
			.timeout(10 * 1000)
			.userAgent(
					"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
			.header("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
			.header("Accept-Language",
					"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
			.header("Accept-Encoding", "gzip,deflate,sdch").get();

			System.out.println(doc.select("td.view_r").text());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void article2Test() throws IOException {
		String url = "https://c.motorgraph.com/free/682104";

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

			System.out.println(doc.select("article > div").text());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		MotorgraphArticleTest test = new MotorgraphArticleTest();
		test.article2Test();
	}
}