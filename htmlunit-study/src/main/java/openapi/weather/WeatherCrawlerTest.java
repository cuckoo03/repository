package openapi.weather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * 수집가능
 * 
 * @author cuckoo03
 *
 */
public class WeatherCrawlerTest {

	@Test
	public void categoryTest() throws IOException {

		String url = "http://www.kma.go.kr/weather/climate/past_cal.jsp?stn=216&yy=2016&mm=12&obs=1&x=22&y=16";
		Document doc = Jsoup.connect(url).get();

		// System.out.println(doc.html());

		Element table = doc.select(".table_develop").first();
		List<Element> tdList = table.select("td");
		List<Map<String, String>> weatherDailyList = new ArrayList<>();
		int day = 1;
		for (Element td : tdList) {
			String s = td.text();
			if (Character.isSpaceChar(s.charAt(0))) {
				continue;
			}
			if (!s.contains("평균기온")) {
				continue;
			}
			
			Map<String, String> map = new HashMap<>();
			map.put("day", String.valueOf(day++));
			
			System.out.println(s);

			int index = s.indexOf("최고기온");
			String avg = s.substring(0, index).trim().replace("평균기온:", "")
					.replace("℃", "");
			System.out.println(avg);
			map.put("avg", avg);

			String max = s.substring(index, s.indexOf("최저기온")).trim()
					.replace("최고기온:", "").replace("℃", "");
			System.out.println(max);
			map.put("max", max);

			index = s.indexOf("최저기온");
			String min = s.substring(index, s.indexOf("평균운량")).trim()
					.replace("최저기온:", "").replace("℃", "");
			System.out.println(min);
			map.put("min", min);

			index = s.indexOf("일강수량");
			String rainfall = s.substring(index, s.length()).trim()
					.replace("일강수량:", "").replace("℃", "").replace("-", "")
					.replace("mm", "");
			System.out.println(rainfall);
			map.put("rainfall", rainfall);
			
			weatherDailyList.add(map);
		}
	}
}
