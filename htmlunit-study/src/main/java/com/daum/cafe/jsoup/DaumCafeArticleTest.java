package com.daum.cafe.jsoup;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

/**
 * 게시물 파싱 가능.
 * 
 * @author cuckoo03
 *
 */
public class DaumCafeArticleTest {
//	 final String CAFE_URL = "http://cafe.daum.net/_c21_/bbs_search_read?grpid=xZQR&fldid=38cw&datanum=46916&q=a";
	final String CAFE_URL = "http://cafe.daum.net/_c21_/bbs_read?grpid=1VDkF&fldid=VXbl&datanum=435&_referer=V7kfJwkeLEE_BcNpTVULwWlKmNlFJ16Vno3o_FWAA1k0"; String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	Document page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		this.page = Jsoup.connect(CAFE_URL).get();
		System.out.println(page.html());
		 parsingArticle();
	}

	private void parsingArticle() {
		System.out.println("title:"
				+ page.select("div.subject > span.b").text());
		System.out.println("writerid:"
				+ page.select("div.article_writer > a").first());
		System.out.println("viewcount:"
				+ page.select("div.article_writer > span.p11").get(0));
		System.out.println("recommand:"
				+ page.select("div.article_writer > span.p11").get(1));
		System.out.println("createdate:"
				+ page.select("div.article_writer > span[class=p11 ls0]")
						.first());
		Document content = Jsoup.parse(page.select("xmp[id=template_xmp]")
				.first().text());
		System.out.println("content:" + content.text());

	}

	private void print(String html) {
		System.out.println(html);
	}
}
