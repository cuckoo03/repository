package com.naver.cafe.jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

/**
 * 게시물별 url 파싱 가능, 자바스크립트 파싱 미지원으로 인해 게시물 파싱 불가
 * 
 * @author cuckoo03
 *
 */
public class NaverCafeListTest {
	final String CAFE_URL = "http://section.cafe.naver.com/ArticleSearch.nhn?query=%EC%95%84%EC%9D%B4%ED%8F%B0&where=&x=0&y=0";// pc
	// final String CAFE_URL =
	// "http://m.cafe.naver.com/SectionArticleSearch.nhn?page=1&sortBy=0&query=%EC%95%84%EC%9D%B4%ED%8F%B0";//mobile
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	Document page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException, JSONException {
		JSONObject json = new JSONObject();
		json.put("query", "아이폰");
		json.put("sortBy", "1");
//		json.put("period", new String[] {});
//		json.put("menuType", new String[] { "0" });
//		json.put("searchBy", "0");
//		json.put("duplicate", "false");
		json.put("inCafe", "");
		json.put("withOutCafe", "");
		json.put("includeAll", "");
//		json.put("exclude", "");
		json.put("include", "");
		json.put("exact", "");
		json.put("page", "1");
		@SuppressWarnings("deprecation")
		String encode = URLEncoder.encode(json.toString());
		@SuppressWarnings("deprecation")
		String enKey = URLEncoder.encode("아이폰");
		System.out
				.println("http://section.cafe.naver.com/ArticleSearch.nhn?query="
						+ enKey + "&where=&x=0&y=0#" + encode);

		@SuppressWarnings("deprecation")
		String decode = URLDecoder.decode(encode);
		System.out.println(decode);

		System.exit(1);
		// this.page = Jsoup.connect(CAFE_URL).get();
		// print(page.html());
		// parsingArticles();
	}

	private void parsingArticles() throws IOException {
		List<Element> list = page.select("ul[id=ArticleSearchResultArea] > li");
		for (Element item : list) {
			String articleUrl = item.select("dl > dt > a").first().attr("href");
			System.out.print("articleUrl=" + articleUrl);

			System.out.print(", ");

			String articleNum = articleUrl.substring(
					articleUrl.lastIndexOf("/") + 1, articleUrl.length());
			System.out.print("articleNum=" + articleNum);

			System.out.print(", ");

			String clubIdTag = item.select("dl > dd.txt_block > a").first()
					.attr("class"); // ex)N=a:art.url,i:clubId,r:1
			String clubId = clubIdTag.split(",")[1].replace("i:", "");
			System.out.print("clubId=" + clubId);

			System.out.print(", ");

			final String ARTICLE_URL = "http://m.cafe.naver.com/ArticleRead.nhn?clubid="
					+ clubId + "&articleid=" + articleNum;
			System.out.println("parsing articleUrl = " + ARTICLE_URL);
			// parsingArticle(ARTICLE_URL);
		}
	}

	private void parsingArticle(String ariticleUrl) throws IOException {
		Document doc = Jsoup.connect(ariticleUrl).userAgent(userAgent)
				.referrer(ariticleUrl).timeout(5 * 1000).get();
		print(doc.html());
	}

	private void print(String html) {
		System.out.println(html);
	}
}
