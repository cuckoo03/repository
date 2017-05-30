package com.movie.pandoratv.jsoup;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

/**
 * pandora TV 인기 동영상 수집 테스트
 * @author cuckoo03
 *
 */
public class JSoupPandoraTVTest {
	private Document get(String url, boolean source) throws IOException {
		Document doc = Jsoup.connect(url).get();
		if (source) {
			System.out.println(doc.html());
		}
		
		return doc;
	}
	
	/**
	 * 인기 게시물 목록 수집 테스트
	 * @throws IOException
	 */
	@Test
	@Ignore
	public void crawlHotMoviesCrawlTest() throws IOException {
		String hotMovies = "http://www.pandora.tv/search/searchResult?query=%EC%82%BC%EC%84%B1&type=movie";
		
		Document doc = get(hotMovies, true);
		/*
		List<Element> list = doc.select("div.cds > div.cds_type");
		for (Element item : list) {
			String url = item.select("a.cds_thm").attr("href");
			System.out.println("url : " + url);
			
			String title = item.select("a.cds_thm img").attr("alt");
			System.out.println("title : " + title);
			
			String pic = item.select("a.cds_thm img").attr("src");
			System.out.println("pic : " + pic);
			
			String hit = item.select("dd.meta > span.hit").text();
			System.out.println("hit : " + hit);
			
			
			System.out.println("");
		}
		*/
	}
	
	/**
	 * 인기 게시물 항목 세부 수집 테스트
	 * 수집 가능 항목:게시물url, 게시물제목, , 조회수, 좋아요, 싫어요 ,본문
	 * @throws IOException
	 */
	@Test
	public void crawlHotMoviesDetailCrawlTest() throws IOException {
		String articleUrl = "http://www.pandora.tv/view/xyzformula/54542489/#38661554_new";
		Document doc = get(articleUrl, false);
		
		String hit = doc.select("dd.ask").text();
		System.out.println(hit);
		
		
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
