package com.movie.navertv.jsoup;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 네이버TV 인기 동영상 수집 테스트
 * @author cuckoo03
 *
 */
public class JSoupNaverTVTest {
	private Document get(String url, boolean source) throws IOException {
		Document doc = Jsoup.connect(url).get();
		if (source) {
			System.out.println(doc.html());
		}
		
		return doc;
	}
	
	/**
	 * 인기 게시물 목록 수집 테스트
	 * 수집 가능 항목:게시물 url, 게시물 제목, 게시물 동영상 길이, 
	 * 게시물 조회수, 좋아요수, 순위, 증감수 
	 * @throws IOException
	 */
	@Test
	@Ignore
	public void crawlHotMoviesCrawlTest() throws IOException {
		String hotMovies = "http://tv.naver.com/r/category/music";
		
		Document doc = get(hotMovies, false);
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
	}
	
	/**
	 * 인기 게시물 항목 세부 수집 테스트
	 * 수집 가능 항목:게시물url, 게시물제목, , 조회수, 좋아요, 싫어요 ,본문
	 * @throws IOException
	 */
	@Test
	@Ignore
	public void crawlHotMoviesDetailCrawlTest() throws IOException {
		String articleUrl = "http://tv.naver.com/v/1450671";
		Document doc = get(articleUrl, false);
		
		String title = doc.select("h3._clipTitle").text();
		System.out.println(title);
		
		String hit = doc.select("div.title_info > span.play").text();
		System.out.println(hit);
		
		String createDate = doc.select("span.date").text();
		System.out.println(createDate);
	}

	/**
	 * 채널 홈 상세 정보 수집 테스트
	 * 수집가능항목:구독자수,전체재생수,전체좋아요수,동영상수,재생목록수,채널개설일
	 * @throws IOException
	 */
	@Test
	public void crawlChannelDetailTest() throws IOException {
		String url = "http://tv.naver.com/kbs.sectionchief";
		Document doc = get(url, false);
		
		String numOfSubcribe = doc.select("ul.view span.cnt").get(0).text();
		System.out.println("numofSubcribe" + numOfSubcribe);
	}
}
