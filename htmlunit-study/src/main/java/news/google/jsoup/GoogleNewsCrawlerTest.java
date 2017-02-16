package news.google.jsoup;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

public class GoogleNewsCrawlerTest {
	@Test
	public void mainTest() throws IOException {
		String url = "https://news.google.co.kr/?edchanged=1&ned=kr&authuser=0";
		Document doc = Jsoup.connect(url).get();
		System.out.println(doc.html());
	}

	@Ignore
	@Test
	public void categoryTest() throws IOException {
		String url = "http://news.yahoo.com/us/";
		Document doc = Jsoup.connect(url).get();

		List<Element> el = doc.select("h3[class=fw-b fz-s lh-115] > a");
		System.out.println(el);
	}

	@Ignore
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
