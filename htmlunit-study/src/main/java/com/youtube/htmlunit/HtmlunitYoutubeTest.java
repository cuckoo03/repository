package com.youtube.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

public class HtmlunitYoutubeTest {
	WebClient webClient;
	HtmlPage page;

	public void get(String url) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		webClient = HtmlUnitFactory.createDefaultWebClient();
		HtmlPage page1 = webClient.getPage(url);
		System.out.println(page1.asXml());
	}

	/**
	 * 인기 게시물 항목 세부 수집 테스트 
	 * 수집 가능 항목:게시물url, 게시물제목, 게시물길이, 채널url, 채널명, 구독자수, 조회수, 좋아요수, 실어요수, 업로드일자, 본문
	 * 
	 * @throws IOException
	 */
	@Test
	public void crawlHotMoviesDetailCrawlTest() throws IOException {
		String articleUrl = "https://www.youtube.com/watch?v=L-2M_-QLs8k";
		get(articleUrl);
	}
}
