package news.yahoo.jsoup;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

public class YahooNewsCrawlerTest {
	@Ignore
	@Test
	public void mainTest() throws IOException {
		String url = "http://news.yahoo.com/";
		Document doc = Jsoup.connect(url).get();
		System.out.println(doc.html());
	}

	@Test
	public void categoryTest() throws IOException {
		String url = "http://news.yahoo.com/us/";
		Document doc = Jsoup.connect(url).get();

		List<Element> el = doc.select("h3[class=fw-b fz-s lh-115] > a");
		System.out.println(el);
	}

	@Test
	public void articleTest() throws IOException {
		String url = "http://news.yahoo.com/california-college-attacker-inspired-isis-acted-alone-fbi-004429482.html";
		Document doc = Jsoup.connect(url).get();
		String content = doc.select("div[itemType=http://schema.org/Article]")
				.text();
		System.out.println(content);
		System.out.println("-------------------------------------------------");

	}
}
