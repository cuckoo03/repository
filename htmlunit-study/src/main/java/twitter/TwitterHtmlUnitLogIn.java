package twitter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * 트위터 검색어를 포함한 조회페이지 호출후 트윗글 마지막 추출 트윗글은 검색어에 해당하는 계정이 존재할 때와 존재하지 않을때의 html
 * tag 형태가 다르므로 파싱에 주의해야 한다. 키보드 아래 화살표를 프로그래밍적으로 처리했으나 원하는 다음 트윗글이 조회되지 않았다.
 * webdriver를 이용해 테스트할 예정
 * 
 * @author cuckoo03
 *
 */
public class TwitterHtmlUnitLogIn {
	HtmlPage page;

	@SuppressWarnings("deprecation")
	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		WebClient webClient = HtmlUnitFactory.createDefaultWebClient();

		page = webClient.getPage("https://twitter.com/oisoo/followers");
		System.out.println(page.asXml());

		HtmlOrderedList olList = (HtmlOrderedList) page
				.getElementById("stream-items-id");

		// getChild2(olList);

		// finish the virtual web browser
		webClient.closeAllWindows();
	}

	private void getChild(HtmlOrderedList olList) {
		DomNodeList<DomNode> list = olList.getChildNodes();
		for (DomNode node : list) {
			System.out.println(node.asXml());
			System.out.println("----------");
		}
		System.out.println(olList.getChildNodes().size());
	}

	private void getChild2(HtmlOrderedList olList) throws IOException {
		Iterator<DomElement> iter = olList.getChildElements().iterator();
		DomElement last = null;
		while (iter.hasNext()) {
			last = iter.next();
			// System.out.println(last.asXml());
			System.out.println("---------------------------------------------");
		}
		System.out.println(page.setFocusedElement((HtmlElement) last));
		System.out.println("childCount:" + olList.getChildElementCount());

		/*
		 * ScriptResult result = page
		 * .executeJavaScript("window.scrollBy(0, 3000)"); HtmlPage page2 =
		 * (HtmlPage) result.getNewPage();
		 * 
		 * HtmlOrderedList olList2 = (HtmlOrderedList) page2
		 * .getElementById("stream-items-id");
		 * 
		 * System.out.println("childCount:" + olList2.getChildElementCount());
		 */

	}
}
