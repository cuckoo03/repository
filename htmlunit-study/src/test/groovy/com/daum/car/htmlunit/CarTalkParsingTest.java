package com.daum.car.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * <p>
 * 다음 자동차 카테고리 한줄토크 게시글 수집 테스트
 * </p>
 * 타겟 페이지 이동 완료, 게시글 태그 탐색 완료, 페이지 네비게이션 이동 완료
 * 
 * @author cuckoo03
 *
 */
public class CarTalkParsingTest {
	// static final String CAFE_URL =
	// "http://auto.daum.net/newcar/make/model/talk.daum?id=602843&modelid=3776";
	static final String CAFE_URL = "http://auto.daum.net/comment.daum?modelid=3776&amp;viewType=model&amp;netizen=F&amp;id=602843";
	WebClient webClient;
	HtmlPage page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		webClient = HtmlUnitFactory.createDefaultWebClient();

		// WAS 를 띄운다. 테스트 하고자 하는 페이지로 접근하여 데이타를 받아온다
		page = webClient.getPage(CAFE_URL);

		moveOneLineTalkComments();
		travelComments();
		moveNextPage();
		print();
	}

	private void moveNextPage() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		@SuppressWarnings("unchecked")
		List<HtmlAnchor> anchors = (List<HtmlAnchor>) page
				.getByXPath("//div//div[@class='paging']//a");
		HtmlAnchor anchor;
		for (int i = 0; i < anchors.size(); i++) {
			if (i == 0)
				continue;

			anchor = anchors.get(i);
			System.out.println(anchor.getHrefAttribute());
			this.page = anchor.click();
			break;
		}
	}

	private void travelComments() {
		HtmlUnorderedList ul = page.getFirstByXPath("//div//ul");
		System.out.println(ul.getChildElementCount());
		System.out.println(ul.getNextElementSibling());
		System.out.println(ul.getNextSibling()); // null
		DomNodeList<DomNode> nodeList = ul.getChildNodes();// DomText,
															// HtmlListItem
		Iterator<DomNode> iter = ul.getChildren().iterator(); // HtmlListItem
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}

	private void moveOneLineTalkComments()
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		final List<FrameWindow> window = page.getFrames();
		String url = null;
		for (FrameWindow item : window) {
			Page page = item.getEnclosingPage();
			System.out.println(page.getUrl());
			url = page.getUrl().toString();
			break;
		}

		this.webClient.getPage(url);
	}

	private void print() {
		System.out.println(page.getUrl());
		System.out.println(page.asXml());
	}
}
