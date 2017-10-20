package com.naver.news;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

/**
 * 
 * @author cuckoo03
 *
 */
public class NaverNewsArticleTest {
	final String ARTICLE_URL = "https://section.blog.naver.com/ajax/SearchList.nhn?countPerPage=7&currentPage=2&endDate=&keyword=%EC%A7%80%EA%B8%88&orderBy=recentdate&startDate=&type=post";
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	Document page;

	JSONObject testJson = new JSONObject();
	private Response res;
	
	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		this.page = null;
		res = Jsoup
				.connect(ARTICLE_URL)
				.timeout(10 * 1000)
				.referrer("https://section.blog.naver.com/Search/Post.nhn?keyword=%EB%84%A4%EC%9D%B4%EB%B2%84")
				.userAgent(
						"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
				.header("Accept",
						"application/json;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Language",
						"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
				.header("Accept-Encoding", "gzip,deflate,sdch").ignoreContentType(true).execute();

		// print(page.html());
		crawlArticle();
	}

	private void crawlArticle() throws IOException {
		// String content = page.select("div#articeBody").text();
		System.out.println(Jsoup.parse(res.parse().outerHtml()));
		String s = page.html();
//		s = s.replaceAll("&quot;", "\"");
//		s = s.replaceAll("=\\\"", "=");
//		s = s.replaceAll("\\\">", ">");
//		s = s.replaceAll("\n", "");
//		s = s.replaceAll("\\&quot;", "");
//		String s = page.html();
		print(s);
		String content = page.select("div[id=articleBodyContents]").text();
//		System.out.println("content=" + content);
	}

	private void print(String html) {
		System.out.println(html);
	}
}
