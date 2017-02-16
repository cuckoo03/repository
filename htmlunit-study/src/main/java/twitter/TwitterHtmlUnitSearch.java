package twitter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.htmlunit.common.HtmlUnitFactory;

/**
 * 트위터 검색 초기화면에서 검색어 입력후 검색 버튼 클릭
 * @author cuckoo03
 *
 */
public class TwitterHtmlUnitSearch {
	@Test
	public void test() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		WebClient webClient = HtmlUnitFactory.createDefaultWebClient();

		HtmlPage page = webClient.getPage("https://twitter.com/oisoo/followers");

		System.out.println(page.asXml());
		
		HtmlInput qInput = (HtmlInput) page.getElementById("search-home-input");
		qInput.setValueAttribute("하둡");

		List<DomElement> divlist = page.getElementsByTagName("div");
		HtmlButton button = null;
		for (DomElement item : divlist) {
			if ("operators-container".equals(item.getAttribute("class"))) {
				@SuppressWarnings("unchecked")
				List<HtmlButton> buttonList = ((List<HtmlButton>) item
						.getByXPath("//button[@class='button btn primary-btn submit selected search-btn']"));
				if (buttonList.size() == 1) {
					button = buttonList.get(0);
					break;
				}
			}
		}

		HtmlPage next = button.click();
		
		HtmlOrderedList olList = (HtmlOrderedList) next.getElementById("stream-items-id");
		System.out.println(olList);

		// finish the virtual web browser
		webClient.closeAllWindows();
	}
}
