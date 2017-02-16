package com.tapacross.sns.instagram.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * 인스타그램 개임 홈페이지 게시물 조회
 * 
 * @author cuckoo03
 *
 */
public class InstagramPage {
	WebClient webClient;
	HtmlPage page;

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		webClient = HtmlUnitFactory.createDefaultWebClient();

		// java.util.logging.Logger.getLogger("com.gargoylesoftware")
		// .setLevel(Level.OFF);

		final HtmlPage page1 = webClient
				.getPage("https://instagram.com/jazzflow1/");

		 System.out.println(page1.asXml());

		// HTML 에서 form 객체를 가져온다.

		 /*
		List<HtmlListItem> list = (List<HtmlListItem>) page1
				.getByXPath("//li[@class='-cx-PRIVATE-PostInfo__comment']");
		System.out.println(list.size());

		for (HtmlListItem li : list) {
			List<HtmlElement> anchors = li.getElementsByTagName("a");
			for (HtmlElement anchor : anchors) {
				System.out.println(anchor.getAttribute("href"));
				break;
			}
			
			List<HtmlElement> spans = li.getElementsByTagName("span");
			for (HtmlElement span : spans) {
				System.out.println(span.asText());
				break;
			}
			System.out.println("--------");
		}
		*/

		/*
		 * final HtmlForm form = page1.getFormByName("myform");
		 * 
		 * final HtmlTextInput usernameInput =
		 * page1.getHtmlElementById("lfFieldInputUsername");
		 * 
		 * // Button 객체를 가져온다. final HtmlSubmitInput button =
		 * form.getInputByName("button"); // Input text 객체를 가져온다. final
		 * HtmlTextInput textField = form.getInputByName("userid");
		 * textField.setValueAttribute("값변경"); // Input text 값을 변경한다.
		 * 
		 * // 버튼 클릭과 같은 기능을 한다. javascript 함수 호출이나 submit 기능을 // 구현해 놓았다면 그대로
		 * 실행될 것이다. final HtmlPage page2 = button.click();
		 */
	}
}
