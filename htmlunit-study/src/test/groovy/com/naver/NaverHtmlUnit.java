package com.naver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * <p>
 * 네이버 카페 게시글 수집 테스트
 * </p>
 * 카페 로그인 완료, 타켓 페이지 이동 완료, 게시글 접근 완료, 페이지 네비게이션 이동 완료
 * 
 * @author cuckoo03
 *
 */
public class NaverHtmlUnit {
	static final String CAFE_URL = "http://cafe.naver.com/malltail";
	WebClient webClient;
	HtmlPage page;

	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		webClient = new WebClient(BrowserVersion.FIREFOX_38);
		webClient.getOptions().setJavaScriptEnabled(true);

		// WAS 를 띄운다. 테스트 하고자 하는 페이지로 접근하여 데이타를 받아온다
		page = webClient.getPage(CAFE_URL);

		HtmlListItem alink = page
				.getFirstByXPath("//li[@class='gnb_login_li']");
		page = alink.click();

		moveLoginPage();
		moveCafe();
		moveTargetPage();
		// moveFirstArticle();
		moveNextPage();

		webClient.close();
	}

	private void moveNextPage() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		HtmlTable naviHtmlTable = page
				.getFirstByXPath("//table[@class='Nnavi']");
		HtmlTableRow row;
		HtmlTableCell cell;
		for (int i = 0; i < naviHtmlTable.getRowCount(); i++) {
			row = naviHtmlTable.getRow(i);
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
			System.out.println(CAFE_URL + href);
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

	private void moveTargetPage() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		HtmlAnchor alink = page.getFirstByXPath("//a[@id='menuLink98']");
		page = alink.click();
	}

	private void moveCafe() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		page = webClient.getPage("http://cafe.naver.com/malltail");
	}

	private void moveLoginPage() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		page = webClient.getPage("https://nid.naver.com/nidlogin.login");

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
