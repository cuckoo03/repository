package com.zum.keyword.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class ZumKeywordTest {
	@Test
	public void test() {
		String url = "http://www.ytn.co.kr/_sn/0117_201802061508241812";
		String url1 = "http://isplus.live.joins.com/news/article/article.asp?total_id=22339426&cloc=";
		try {
			Document doc = Jsoup.connect(url1).get();
			System.out.println(doc.html());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
