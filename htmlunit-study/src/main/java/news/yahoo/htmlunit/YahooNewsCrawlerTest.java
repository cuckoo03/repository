package news.yahoo.htmlunit;

import java.io.IOException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

public class YahooNewsCrawlerTest {
	@Test
	public void test() throws IOException {
		String url = "http://news.yahoo.com/";
		WebClient webClient = HtmlUnitFactory.createDefaultWebClient();

		// WAS 를 띄운다. 테스트 하고자 하는 페이지로 접근하여 데이타를 받아온다
		final HtmlPage page = webClient.getPage(url);

		System.out.println(page.asXml());

	}
}
