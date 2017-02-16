package com.daum.blog.jsoup;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * 다음 블로그 목록보기 파싱 테스트
 * 파싱은 문제없으나 creation date 데이터가 html태그에 없음.
 * 
 * @author cuckoo03
 *
 */
public class DaumBlogListViewTest {
	String url = "http://blog.daum.net/_blog/ArticleCateList.do?blogid=0ER7F&vblogid=&beforePage=1&maxarticleno=12878238&minarticleno=11953595&maxregdt=20100524132816&minregdt=20080629025254&currentPage=1&listScale=&viewKind=&dispkind=B2202&CATEGORYID=0&categoryId=0&articleno=&regdt=&date=&calv=&chgkey=YS4hP4WWxLwe9gL.RqtxzkO1xy.MUt4AK9aNu7MPlsI0&totalcnt=41";
	String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	private Document doc;

	@Test
	public void test() {
		try {
			doc = Jsoup.connect(url).userAgent(userAgent).referrer(url)
					.timeout(5 * 1000).get();
			System.out.println(doc.html());

			List<Element> articleList = doc
					.select("table[class=artiList] > tbody > tr > td[class=title] > h4[class=cB_title]");
			System.out.println("size:" + articleList.size());
			for (Element el : articleList) {
				System.out.println(el);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
