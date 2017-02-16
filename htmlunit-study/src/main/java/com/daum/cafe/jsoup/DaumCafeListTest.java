package com.daum.cafe.jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

/**
 * 게시물별 url 파싱 가능
 * 
 * @author cuckoo03
 *
 */
public class DaumCafeListTest {
	final String CAFE_URL = "http://top.cafe.daum.net/_c21_/search?search_opt_radio=board&q=%EC%95%84%EC%9D%B4%ED%8F%B0&search_opt=board&w=cafe&f=section&SA=daumsec&m=&lpp=10&nil_profile=vsearch&nil_src=cafe";
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	Document page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		this.page = Jsoup.connect(CAFE_URL).get();
		// print();
		parsingArticles();
	}

	private void parsingArticles() throws IOException {
		List<Element> anchors = page
				.select("ul[class=list_scafe] > li > div > strong > a");
		for (Element anchor : anchors) {
			// 치환된 특수문자(&)를 원래대로 복원
//			parsingArticle(anchor.attr("href").replaceAll("&amp;", "&"));
			parsingArticle("http://cafe.daum.net/life5484/VXbl/435");
		}
	}

	private void parsingArticle(String articleUrl) throws IOException {
		System.out.println("article Url=" + articleUrl);

		Document doc = Jsoup.connect(articleUrl).userAgent(userAgent)
				.referrer(articleUrl).timeout(5 * 1000).get();
		System.out.println("iframe src="
				+ doc.select("frame[id=down]").first().attr("src"));

		Document articleDoc = Jsoup.connect(
				"http://cafe.daum.net"
						+ doc.select("frame[id=down]").first().attr("src"))
				.get();
		print("base uri=" + articleDoc.baseUri());

	}

	private void print(String html) {
		System.out.println(html);
	}
}
