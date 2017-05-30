package com.movie.daumtv.htmlunit;

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

/**
 * daumTV 인기 동영상 수집 테스트
 * @author cuckoo03
 *
 */
public class HtmlunitDaumTVTest {
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
	 * 인기 게시물 목록 수집 테스트
	 * 수집 가능 항목:게시물 url, 게시물 제목, 게시물 조회수, 댓글수, 재생시간
	 * 대표이미지 
	 * @throws IOException
	 */
	@Test
	@Ignore
	public void crawlHotMoviesCrawlTest() throws IOException {
		String hotMovies = "http://tvpot.daum.net/best/Top.do?from=gnb";
		
		Document doc = get(hotMovies, false);
		List<Element> list = doc.select("ul#clipList > li");
		for (Element item : list) {
			String href = item.select("dd.title > a").attr("href");
			System.out.println(href);
			
			String title = item.select("dd.title > a").attr("title");
			System.out.println(title);
			
			System.out.println("");
		}
	}
	
	@Test
	public void commentCrawlTest() throws IOException {
		String articleUrl = "http://tvpot.daum.net/mypot/json/GetCommentList.do?clipid=81741909";
		Document doc = get(articleUrl, true);
	}
	
	/**
	 * 인기 게시물 항목 세부 수집 테스트
	 * 수집불가 확인
	 * @throws IOException
	 */
	@Test
	@Ignore
	public void crawlHotMoviesDetailCrawlTest() throws IOException {
		String articleUrl = "http://tvpot.daum.net/mypot/View.do?ownerid=P.9W_h7v-vo0&clipid=81741909";
		Document doc = get(articleUrl, true);
		
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
