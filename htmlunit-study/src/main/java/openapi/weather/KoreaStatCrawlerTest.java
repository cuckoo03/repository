package openapi.weather;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * @author cuckoo03
 *
 */
public class KoreaStatCrawlerTest {

	@Test
	public void categoryTest() throws IOException {

		String url = "http://kostat.go.kr/incomeNcpi/cpi/cpi_td/2/1/index.action?bmode=cpidtval";
		Document doc = Jsoup.connect(url).get();

		System.out.println(doc.html());
	}

	@Test
	public void articleTest() throws IOException {

	}
}
