package news.google.htmlunit;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

public class GoogleNewsCrawlerTest {
	@Ignore
	@Test
	public void mainTest() throws IOException {
		String url = "https://news.google.com/?edchanged=1&ned=us&authuser=0";
		WebClient webClient = HtmlUnitFactory.createDefaultWebClient();

		// WAS 를 띄운다. 테스트 하고자 하는 페이지로 접근하여 데이타를 받아온다
		final HtmlPage page = webClient.getPage(url);
		Document doc = Jsoup.parse(page.asXml());

		List<Element> el = doc
				.select("div.esc-lead-article-title-wrapper > h2.esc-lead-article-title > a");
		for (Element e : el) {
			System.out.println(e);
		}
	}

	@Test
	public void categoryTest() throws IOException {
		String url = "http://whotv.com/2016/03/17/harry-reid-blames-gop-moral-cowardice-for-rise-of-donald-trump/";
		Document doc = Jsoup.connect(url).get();
		System.out.println(doc.html());
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
