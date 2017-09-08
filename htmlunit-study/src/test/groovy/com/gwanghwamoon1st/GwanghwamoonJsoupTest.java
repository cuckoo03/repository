package com.gwanghwamoon1st;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;

/**
 * https://www.gwanghwamoon1st.go.kr/policyHot/policySugView.php?p_idx=60187&seq=106240&p_cate1=P12&page=1
 * https://www.gwanghwamoon1st.go.kr/policyHot/policySugView.php?p_idx=60187&seq=106240&p_cate1=P12&page=1
 * @author admin
 *
 */
public class GwanghwamoonJsoupTest {
	@Test
	@Ignore
	public void testList() {
		String url = "https://www.gwanghwamoon1st.go.kr/policyHot/policyHot02.php?page=1&sch_type=&sch_text=&mcate=P12";
		try {
			Document doc = Jsoup.connect(url).get();
			System.out.println(doc.html());
			Elements elements = doc.select("table tr td.textPre > a");
			for (Element e : elements) {
//				System.out.println(e);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testArticle() {
			String url = "https://www.gwanghwamoon1st.go.kr/policyHot/policySugView.php?p_idx=60187&seq=106240&p_cate1=P12&page=1";
		try {
			Document doc = Jsoup.connect(url).get();
			System.out.println(doc.html());
			Elements elements = doc.select("div.policyView table tr td div.text");
			for (Element e : elements) {
				System.out.println(e);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
