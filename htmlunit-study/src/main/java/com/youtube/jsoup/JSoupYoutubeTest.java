package com.youtube.jsoup;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 유튜브 인기 동영상 수집 테스트
 * @author cuckoo03
 *
 */
public class JSoupYoutubeTest {
	private Document get(String url, boolean source) throws IOException {
		Document doc = Jsoup.connect(url).get();
		if (source) {
			System.out.println(doc.html());
		}
		
		return doc;
	}
	
	/**
	 * 인기 게시물 목록 수집 테스트
	 * 수집 가능 항목:게시물 url, 게시물 제목, 게시물 동영상 길이, 채널url, 채널명, 게시물 조회수 
	 * @throws IOException
	 */
	@Test
	@Ignore
	public void crawlHotMoviesCrawlTest() throws IOException {
		String hotMovies = "https://www.youtube.com/feed/trending";
		
		Document doc = get(hotMovies, false);
		List<Element> list = doc.select("div.yt-lockup-content");
		for (Element item : list) {
			String title = item.select("h3 > a").text();
			System.out.println("title:" + title);
			String href = item.select("h3 > a").attr("href");
			System.out.println("href:" + href);
			System.out.println("");
		}
	}
	
	/**
	 * 인기 게시물 항목 세부 수집 테스트
	 * 수집 가능 항목:게시물url, 게시물제목, 채널url, 채널명, 구독수, 조회수, 좋아요, 싫어요 ,본문
	 * @throws IOException
	 */
	@Test
	public void crawlHotMoviesDetailCrawlTest() throws IOException {
		String articleUrl = "https://www.youtube.com/watch?v=L-2M_-QLs8k";
		Document doc = get(articleUrl, false);
		String title = doc.select("span#eow-title").attr("title");
		System.out.println("title:" + title);
		
		String userUrl = doc.select("div#watch7-user-header > a").attr("href");
		System.out.println("userUrl:" + userUrl);
		
		String channelUrl = doc.select("div.yt-user-info > a").attr("href");
		System.out.println("channelUrl:" + channelUrl);
		
		String channelName = doc.select("div.yt-user-info > a").text();
		System.out.println("channelName:" + channelName);
		
		String subscriberCount = doc.select("span[class=yt-subscription-button-subscriber-count-branded-horizontal yt-subscriber-count]").attr("title");
		System.out.println("sub count:" + subscriberCount);
		
		String viewCount = doc.select("div.watch-view-count").text();
		System.out.println("viewCount:" + viewCount);

		List<Element> sentimentList = doc.select("span > span.yt-uix-clickcard > button > span.yt-uix-button-content");
		for (Element item : sentimentList) {
			String value = item.text();
			System.out.println("senti:" + value);
		}
		
		String upload = doc.select("div#watch-uploader-info > strong ").text();
		System.out.println("upload:" + upload);
		
		String content = doc.select("div#watch-description-text > p").text();
		System.out.println("content:" + content);
	}
	
	/**
	 * 뮤직 게시물 목록 수집 테스트
	 * 수집 가능 항목:제목, url, 길이, 채널명, 채널url, 조회수, content, 
	 * @throws IOException
	 */
	@Test
	public void crawlMusicsCrawlTest() throws IOException {
		String hotMusics = "https://www.youtube.com/channel/UC-9-kyTW8ZkZNDHQJ6FgpwQ";
		Document doc = get(hotMusics, false);
		List<Element> list = doc.select("div.yt-lockup-content");
		for (Element item : list) {
			String title = item.select("h3 > a").text();
			System.out.println("title:" + title);
			String href = item.select("h3 > a").attr("href");
			System.out.println("href:" + href);
			System.out.println("");
		}
	}
	
	@Test
	public void crawlNewsCrawlTest() throws IOException {
		String hotNews = "https://www.youtube.com/channel/UCYfdidRxbB8Qhf0Nx7ioOYw";
		Document doc = get(hotNews, false);
		List<Element> list = doc.select("div.yt-lockup-content");
		for (Element item : list) {
			String title = item.select("h3 > a").text();
			System.out.println("title:" + title);
			String href = item.select("h3 > a").attr("href");
			System.out.println("href:" + href);
			System.out.println("");
		}
	}
	
}
