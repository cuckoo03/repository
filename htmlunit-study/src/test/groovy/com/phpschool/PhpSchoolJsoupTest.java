package com.phpschool;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class PhpSchoolJsoupTest {
	@Test
	public void test() {
		String url = "https://www.phpschool.com/link/talkbox2/1793683";
		try {
			Document doc = Jsoup
					.connect(url)
					.userAgent(
							"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
					.header("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
					.header("Accept-Language",
							"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
					.header("Accept-Encoding", "gzip,deflate,sdch").get();

			// Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8",
			// url);
			System.out.println(doc.html());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
