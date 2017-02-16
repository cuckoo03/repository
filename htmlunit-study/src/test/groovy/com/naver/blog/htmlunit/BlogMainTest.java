package com.naver.blog.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * <p>
 * 네이버 블로그 메인 수집 테스트
 * 실패, 첫번째 게시물만 파싱됨.
 * 네비게이션 파싱 테스트 필요.
 * </p>
 * 
 * @author cuckoo03
 *
 */
public class BlogMainTest {
	final String URL = "http://blog.naver.com/PostList.nhn?blogId=pooqksj2&amp;widgetTypeCall=true";
	// http://cafe.naver.com/malltail.cafe?iframe_url=/ArticleList.nhn%3Fsearch.boardtype=L%26search.menuid=98%26search.questionTab=A%26search.clubid=21820768%26search.totalCount=151%26search.page=1
	WebClient webClient;
	HtmlPage page;

	@SuppressWarnings("deprecation")
	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		webClient = HtmlUnitFactory.createDefaultWebClient();

		page = webClient.getPage(URL);

//		HtmlAnchor alink = page
//				.getFirstByXPath("//li[@class='gnb_login_li']//a[@class='gnb_btn_login']");
//		page = alink.click();

//		LoginPage();

//		moveTargetPage();
		
		print();
		// moveFirstArticle();
//		moveNextPage();

	}

	private void moveTargetPage() throws IOException {
		HtmlAnchor alink = page.getFirstByXPath("//a[@id='menuLink98']");
		page = alink.click();
	}

	private void moveNextPage() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		HtmlTable naviHtmlTable = page
				.getFirstByXPath("//table[@class='Nnavi']");
		HtmlTableRow row;
		HtmlTableCell cell;
		row = naviHtmlTable.getRow(0);
		for (int j = 0; j < row.getCells().size(); j++) {
			cell = row.getCell(j);
			HtmlAnchor anchor = cell.getFirstByXPath("a");
			System.out.println(anchor.getHrefAttribute());

			// 현재 페이지나 이전페이지로 다시 이동하지 않기 위해 네비게이션의 첫번째 페이지는 무시한다.
			if (j == 0)
				continue;

			page = anchor.click();
			print();
		}
	}

	private void moveFirstArticle() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		@SuppressWarnings("unchecked")
		List<HtmlTableCell> tdList = (List<HtmlTableCell>) page
				.getByXPath("//td[@class='board-list']");
		for (HtmlTableCell row : tdList) {
			HtmlAnchor anchor = row.getFirstByXPath("span//span//a");
			if (anchor == null)
				continue;

			String href = anchor.getAttribute("href");
			System.out.println(URL + href);
			page = anchor.click();
			viewArticle();
			break;
		}
	}

	private void viewArticle() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		// page = webClient.getPage(url);
		// page =
		// webClient.getPage("http://cafe.naver.com/malltail.cafe?iframe_url=/malltail/ArticleRead.nhn%3Fclubid=21820768%26page=1%26menuid=98%26boardtype=L%26articleid=2987918");
		// page = webClient.getPage("http://cafe.naver.com/malltail/2987918");
		print();
	}

	private void LoginPage() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		HtmlTextInput id = page.getFirstByXPath("//input[@id='id']");
		id.setValueAttribute("nathalieshin");

		HtmlPasswordInput pw = page.getFirstByXPath("//input[@id='pw']");
		pw.setValueAttribute("Newyork7626");

		HtmlSubmitInput submit = page
				.getFirstByXPath("//input[@class='int_jogin']");

		page = submit.click();
	}

	private void print() {
		System.out.println(page.getUrl());
		System.out.println(page.asXml());
	}
}
