package com.youtube.htmlunit;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

public class HtmlunitYoutubeTest {
	private Document get(String url, boolean source) throws IOException {
		WebClient webClient = HtmlUnitFactory.createDefaultWebClient();
		final HtmlPage page = webClient.getPage(url);
		Document doc = Jsoup.parse(page.asXml());
		if (source) {
			System.out.println(doc.html());
		}
		
		webClient.closeAllWindows();
		
		return doc;
	}

	/**
	 * 인기 게시물 항목 세부 수집 테스트 
	 * 수집 가능 항목:게시물url, 게시물제목, 게시물길이, 채널url, 채널명, 구독자수, 조회수, 좋아요수, 실어요수, 업로드일자, 본문
	 * 
	 * @throws IOException
	 */
	@Test
	public void crawlHotMoviesDetailCrawlTest() throws IOException {
		String articleUrl = "https://www.youtube.com/watch?v=TCDPXisHsfA";
		Document doc = get(articleUrl, true);
	}
}
