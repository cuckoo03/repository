package openapi.weather;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * 수집가능
 * 
 * @author cuckoo03
 *
 */
public class StockCrawlerTest {

	@Test
	public void categoryTest() throws IOException {

		String url = "http://finance.daum.net/quote/kospi.daum?exp=Y&nil_stock=refresh"; // 수집가능
		String url1 = "http://finance.daum.net/item/chart.daum?type=B&code=P1";// 수집불가
		Document doc = Jsoup.connect(url1).get();

		System.out.println(doc.html());
		System.out.println(doc.select("dd#hyenCost > b").text());
	}

	@Test
	public void articleTest() throws IOException {

	}
}
