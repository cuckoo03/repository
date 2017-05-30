package com.movie.navertv.htmlunit;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

public class HtmlunitNaverTVTest {
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
	 * 수집 가능 항목:게시물url, 게시물제목, , 조회수, 좋아요, 싫어요 ,본문
	 * @throws IOException
	 */
	@Test
	public void crawlHotMoviesDetailCrawlTest() throws IOException {
		String articleUrl = "http://tv.naver.com/v/1450671";
		Document doc = get(articleUrl, false);
		
		String title = doc.select("h3._clipTitle").text();
		System.out.println(title);
		
		String hit = doc.select("span.play").text();
		System.out.println(hit);
		
		String createDate = doc.select("span.date").text();
		System.out.println(createDate);
		
		String content = doc.select("p.desc").text();
		System.out.println(content);
		
		String like = doc.select("div.watch_btn em[class=u_cnt _cnt]").text();
		System.out.println(like);
		
		/*
		String title = doc.select("span#eow-title").attr("title");
		System.out.println("title:" + title);
		
		String userUrl = doc.select("div#watch7-user-header > a").attr("href");
		System.out.println("userUrl:" + "https://www.youtube.com" + userUrl);
		
		String channelUrl = doc.select("div.yt-user-info > a").attr("href");
		System.out.println("channelUrl:" + channelUrl);
		
		String channelName = doc.select("div.yt-user-info > a").text();
		System.out.println("channelName:" + channelName);
		
		String subscriberCount = doc.select("span[class=yt-subscription-button-subscriber-count-branded-horizontal yt-subscriber-count]").attr("title");
		System.out.println("subcribe count:" + subscriberCount);
		
		String viewCount = doc.select("div.watch-view-count").text();
		System.out.println("view Count:" + viewCount);

		List<Element> sentimentList = doc.select("span > span.yt-uix-clickcard > button > span.yt-uix-button-content");
		for (Element item : sentimentList) {
			String value = item.text();
			System.out.println("sentiment:" + value);
		}
		
		String upload = doc.select("div#watch-uploader-info > strong ").text();
		System.out.println("upload:" + upload);
		
		String content = doc.select("div#watch-description-text > p").text();
		System.out.println("content:" + content);
		*/
	}

}
