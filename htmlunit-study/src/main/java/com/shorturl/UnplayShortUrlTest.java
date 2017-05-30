package com.shorturl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class UnplayShortUrlTest {
	
	private Document get(String url, boolean source) throws IOException {
		Document doc = Jsoup
				.connect(url)
//				.timeout(5 * 1000)
				.userAgent(
						"User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
				.header("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Language",
						"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
				.header("Accept-Encoding", "gzip,deflate,sdch")
				.get();
		if (source) {
			System.out.println(doc.html());
		}

		return doc;
	}

	@SuppressWarnings("deprecation")
	@Test
	public void muzShortUrlTest() throws IOException {
		String shortUrlPrefix = "http://gaei.tk/index.php?url=";
		String targetUrl = "https://twitter.com/snsenejd/status/857816849735573504";
		String shortUrlPostfix = "&alias=";
		String searchUrl = shortUrlPrefix + URLEncoder.encode(targetUrl)
				+ shortUrlPostfix;
		Document doc = get(searchUrl, false);
		String resultUrl = doc.select("div.input-group input#surl").attr("value");
		System.out.println(resultUrl);
	}
	
	/**
	 * 19글자 shorturl
	 * @throws IOException
	 */
	@Test
	public void durlTest() throws IOException {
		String targetUrl = "https://twitter.com/snsenejd/status/857816849735573504";
		String resultUrl = getShortURL2(targetUrl);
		System.out.println(resultUrl);
	}
	
	public String getShortURL2(String originalURL) {
		String tempSTR = null;
		String shortUrlPrefix = "http://gaei.tk/index.php?url=";
		String shortUrlPostfix = "&alias=";
		@SuppressWarnings("deprecation")
		String searchUrl = shortUrlPrefix + URLEncoder.encode(originalURL)
				+ shortUrlPostfix;
		try {
			Document doc = get(searchUrl, true);
			List<Element> elements = doc.select("div#content > p");
			if (elements.size() > 1) {
				tempSTR = elements.get(1).select("strong a").attr("href");
				// 파싱결과가 정상이 아닐경우 익셉션 전달
				if (tempSTR.length() <= 0) {
					throw new Exception("Short url parsing failed.");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return tempSTR;
	}
	
	/**
	 * 19글자 shorturl
	 * @throws IOException
	 */
	public String getShortURL(String originalURL) {
		String tempSTR = null;
		String shortUrlPrefix = "http://gaei.tk/index.php?url=";
		String shortUrlPostfix = "&alias=";
		@SuppressWarnings("deprecation")
		String searchUrl = shortUrlPrefix + URLEncoder.encode(originalURL)
				+ shortUrlPostfix;
		try {
			Document doc = get(searchUrl, true);
			List<Element> elements = doc.select("div#content > p");
			if (elements.size() > 1) {
				tempSTR = elements.get(1).select("strong a").attr("href");
				// shorturl 파싱결과가 정상이 아닐경우 익셉션 전달
				if (tempSTR.length() <= 0) {
					throw new Exception("Short url parsing failed.");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return tempSTR;
	}
}
