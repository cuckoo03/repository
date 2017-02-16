package twitter;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class TwitterJsoupArticleTest {
	@Test
	public void parseArticlesTest() throws IOException {
		Document doc = Jsoup.connect("https://twitter.com/good_pot").get();

		for (Element el : doc
				.select("div#timeline ol#stream-items-id li div.tweet")) {
			System.out.println(el.html());
			String likeCount = el
					.select("button[class=ProfileTweet-actionButton js-actionButton js-actionFavorite]  span[class=ProfileTweet-actionCountForPresentation]")
					.text();
			System.out.println("like:" + likeCount);
			System.exit(-1);

		}
	}
}
