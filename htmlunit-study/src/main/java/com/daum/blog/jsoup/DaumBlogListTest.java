package com.daum.blog.jsoup;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * 다음 개인 블로그 목록보기 파싱 성공 게시물별 파싱을 하기 위해서는 게시물별 blogid, articleno를 미리 추출해야함 페이징 파싱
 * 처리 성공.
 * 
 * @author cuckoo03
 *
 */
public class DaumBlogListTest {
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	private Document doc;

	@Test
	public void test() {
//		String url = "http://blog.daum.net/_blog/BlogTypeMain.do?blogid=0F6hq&alllist=Y";
		String url = "http://blog.daum.net/_blog/BlogTypeMain.do?blogid=0JHj6&alllist=Y";
		try {
			doc = Jsoup.connect(url).userAgent(userAgent).referrer(url)
					.timeout(5 * 1000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(doc.html());

		// 게시물 url 리스트 파싱
		// 게시물별 추출한 url 형식
		// /_blog/BlogTypeView.do?blogid=0F6hq&amp;articleno=7643452&amp;_bloghome_menu=recenttext

		List<Element> liList = doc.select("div[class=menuBody] > ul > li > a[class=sideListClr]");
		for (Element el : liList) {
			// 다녀간 블로거 항목의 태그까지 파싱이 되엇다면 무시한다.
			if (el.attr("href").contains("http://blog.daum.net"))
				continue;
			String articleUrl = el.attr("href");
			int blogIdStart = articleUrl.indexOf("blogid=");
			int blogIdEnd = articleUrl.indexOf("&articleno");
			String blogId = articleUrl.substring(blogIdStart + 7, blogIdEnd);
			
			int articleNoStart = articleUrl.indexOf("articleno=");
			int articleNoEnd = articleUrl.indexOf("&_bloghome_menu");
			String articleNo = articleUrl.substring(articleNoStart+10,
					articleNoEnd);
			
			String newArticleUrl = "http://blog.daum.net/_blog/hdn/ArticleContentsView.do?blogid="
					+ blogId
					+ "&articleno="
					+ articleNo
					+ "&looping=0&longOpen=";
			
			try {
				doc = Jsoup.connect(newArticleUrl).get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println(doc.html());
			
		}

		// 게시물 파싱
		// http://blog.daum.net/_blog/hdn/ArticleContentsView.do?blogid=0rtws&articleno=724&looping=0&longOpen=

		// 네비게이션 파싱
		List<Element> naviDiv = doc.select("div[id=cNumbering] > a");
		// System.out.println(naviDiv);
		// System.out.println(naviDiv.size());

		// 최소한 필요한 필드
		// http://blog.daum.net/_blog/BlogTypeMain.do?blogid=0F6hq&maxregdt=&minregdt=20151022160000&currentPage=5
		String page2Url = "http://blog.daum.net/_blog/BlogTypeMain.do?blogid=0F6hq&vblogid=&beforePage=1&maxarticleno=7643452&minarticleno=7643431&maxregdt=20151022100011&minregdt=20151001075256&currentPage=2&listScale=20&viewKind=NA&dispkind=B2203&CATEGORYID=0&categoryId=0&articleno=&regdt=&date=&calv=&chgkey=J-eWcRPsvEp7z9U-La9mHXmPXzgDXwi3zZsqK8miW-U0&totalcnt=929";
		try {
			doc = Jsoup.connect(page2Url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		naviDiv = doc.select("div[id=cNumbering] > a");
		// System.out.println(naviDiv);
		// System.out.println(naviDiv.size());

	}
}
