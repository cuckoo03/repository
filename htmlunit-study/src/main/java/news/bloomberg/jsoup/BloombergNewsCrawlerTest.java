package news.bloomberg.jsoup;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

public class BloombergNewsCrawlerTest {
	@Ignore
	@Test
	public void test() throws IOException {
		String url = "http://www.bloomberg.com/technology";
		Document doc = Jsoup.connect(url).get();
		List<Element> el = doc
				.select("div.checkerboard__stories > article > div > h1 > a");
		System.out.println(el);
	}

	@Test
	public void test2() throws IOException {
		String url = "http://www.bloomberg.com/news/articles/2016-03-17/amazon-and-netflix-bet-on-local-tv-to-win-in-europe";
		Document doc = Jsoup.connect(url).get();
		Element el = doc.select("div.article-body__content").first();
		System.out.println(el.text());
	}
}
