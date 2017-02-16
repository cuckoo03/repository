package com.naver.blog.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * 게시물 파싱 테스트 .성공.
 * @author cuckoo03
 *
 */
public class NaverBlogArticleTest {
	@Test
	public void test() {
		//http://blog.naver.com/PostList.nhn?blogId=otrbel54&from=postList&categoryNo=1
//		String url = "http://blog.naver.com/PostView.nhn?blogId=otrbel54&logNo=220507421284&redirect=Dlog&widgetTypeCall=true";
//		String url = "http://blog.naver.com/otrbel54/220507421284"; // 파싱 결과 /PostView.nhn?blogId=otrbel54&amp;logNo=220507421284&amp;redirect=Dlog&amp;widgetTypeCall=true
//		String url = "http://blog.naver.com/myeunmi0321/220567869810?";//"http://12g.co.kr/220519178633";//파싱결과 http://blog.naver.com/tosaju/220519178633?
//		String url = "http://blog.naver.com/ecoplusfilm/220657076632"; // 개인 도메인 url http://blog.naver.com/ecoplusf ilm/220657076632
		String url ="http://blog.naver.com/taec123?Redirect=Log&logNo=220689647733&from=section";
		//http://eunmisylviaparkblog.me/220567869810
		try {
			Document doc = Jsoup.connect(url).get();
			System.out.println(doc.html());
			Element el = doc.select("div[class=post-view pcol2 _param(1)]")
					.first();
			if (el != null)
				System.out.println(el.text());
			
			String frame = doc.select("frame[id=screenFrame]").attr("src");
			doc = Jsoup.connect(frame).get();
			System.out.println(doc.html());

			String frame2 = Jsoup.connect(frame).timeout(10 * 1000).get().select("frame#mainFrame").attr("src");
			doc = Jsoup.connect(frame2).get();
			
			System.out.println(doc.html());
			System.out.println(frame);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
