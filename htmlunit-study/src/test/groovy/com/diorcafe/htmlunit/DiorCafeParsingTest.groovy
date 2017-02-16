package com.diorcafe.htmlunit

import groovy.transform.TypeChecked

import org.junit.Test

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * <p>diorcafe 게시판글 파싱 테스트</p>
 * 로그인 처리 완료, 타겟 페이지 이동 완료, 게시글 태그 탐색 완료, 페이지 네비게이션 이동 완료
 * 
 * @author cuckoo03
 *
 */
@TypeChecked
class DiorCafeParsingTest {
	static final String BASE_URL = "http://www.diorcafe.co.kr"
	static final String URL = "http://www.diorcafe.co.kr/bbs/board.php?bo_table=hotdeal2&page=2&mnb=hotdeal&snb=hotdeal2&page=1"
	static final String BOARD_URL1 = "http://www.diorcafe.co.kr/bbs/board.php?bo_table=hotdeal2&hotdeal&snb=hotdeal2"
	static final String BOARD_URL2 = "http://www.diorcafe.co.kr/bbs/board.php?bo_table=hotdeal&hotdeal&snb=hotdeal"
	static final String BOARD_URL3 = "http://www.diorcafe.co.kr/bbs/board.php?bo_table=oneday&hotdeal&snb=oneday"
	static final String BOARD_URL4 = "http://www.diorcafe.co.kr/bbs/board.php?bo_table=mart_sale&hotdeal&snb=mart_sale"
	static final String BOARD_URL5 = "http://www.diorcafe.co.kr/bbs/board.php?bo_table=gift&hotdeal&snb=gift"

	WebClient webClient
	HtmlPage page

	@Test
	public void test() {
		webClient = HtmlUnitFactory.createDefaultWebClient()

		page = (HtmlPage) webClient.getPage(URL)

		moveLoginPage()
		moveTargetPage()
		getArticles()
		moveNextPage()
	}

	def void print() {
		println page.getUrl()
		println page.asXml()
	}

	def void moveLoginPage() {
		def idInput = (HtmlInput) page.
				getFirstByXPath("//input[@name='mb_id']")
		idInput.setValueAttribute("nathalieshin")

		def pwInput = (HtmlInput) page.
				getFirstByXPath("//input[@name='mb_password']")
		pwInput.setValueAttribute("Newyork7626")

		def loginButton = (HtmlImageInput) page.
				getFirstByXPath("//input[@type='image']")
		page = (HtmlPage) loginButton.click()
	}

	def void moveTargetPage() {
		page = (HtmlPage) webClient.getPage(BOARD_URL1)
	}

	def void getArticles() {
		def anchors = (List<HtmlAnchor>) page.
				getByXPath("//td[@style='word-break:break-all;']//nobr//a") // 게시글 제목 anchor

		println anchors.size()

		for (HtmlAnchor anchor : anchors) {
			// anchor중에서 제목글만 통과하고 댓글은 무시한다
			if (anchor.getChildElementCount() > 0)
				continue

			// ../bbs/board.php?bo_table=hotdeal2&wr_id=195620&snb=hotdeal2&mnb=hotdeal&snb=hotdeal2&snb=hotdeal2
			page = (HtmlPage) webClient.getPage(BASE_URL +
					anchor.getHrefAttribute().replace("..", ""))

			print()
		}
	}

	/**
	 * 1~10페이지까지 url을 추출한다.
	 */
	def void moveNextPage() {
		def anchors = (List<HtmlAnchor>) page.getByXPath("//tbody//tr//td//a")
		println anchors.size()
		for (HtmlAnchor anchor : anchors) {
			// 페이징 anchor가 아니면 무시한다
			if (anchor.getFirstByXPath("b//font") == null) {
				continue;
			}
			println anchor.getHrefAttribute()
		}
	}
}
