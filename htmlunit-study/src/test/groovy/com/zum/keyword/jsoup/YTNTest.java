package com.zum.keyword.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class YTNTest {
	@Test
	public void test() {
		String url = "ZumKeywordTest.java";
		try {
			Document doc = Jsoup.connect(url).get();
			for (Element el : doc.select(
					"ul[class=rank_list d_rank_list d_rank_keyword_0]").select(
					"a[class=d_btn_keyword] > span[class=keyword d_keyword]")) {

				System.out.println("el:" + el.text());
			}
			System.out.println();
			for (Element el : doc.select(
					"ul[class=rank_list d_rank_list d_rank_keyword_1]").select(
					"a[class=d_btn_keyword] > span[class=keyword d_keyword]")) {

				System.out.println("el:" + el.text());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
