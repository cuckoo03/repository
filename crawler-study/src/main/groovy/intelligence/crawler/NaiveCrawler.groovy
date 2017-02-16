package intelligence.crawler

import groovy.transform.TypeChecked

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient

@TypeChecked
class NaiveCrawler {
	private static final String USER_AGENT = "User-agent:"
	private static final String DISALLOW = "Disallow:"
	public static final String REGEXP_HTTP = "<a href=\"http://(.)*\">"
	public static final String REGEXP_RELATIVE = "<a href=\"(.)*\">"

	private int maxNumberUrls
	private long delayBetweenUrls
	private int maxDepth
	private Pattern regexpSearchPattern //집중 크롤링에 사용할 정규식
	private Pattern httpRegexp // url을 추출하는 정규식
	private Pattern relativeRegexp
	// 방문한 url의 목록을 유지, 중복허용하지 않음
	private Map<String, CrawlerUrl> visitedUrls = null
	// 크롤링 허가 여부를 관리
	private Map<String, Collection<String>> sitePermissions = null
	// 방문할 Url queue, 중복url을 허용하지 않음
	private Queue<CrawlerUrl> urlQueue = null
	private BufferedWriter crawlOutput = null
	private BufferedWriter crawlStatistics = null
	// 연관된 컨텐츠가 없는 url을 기록
	private BufferedWriter crawlIgnoreOutput = null
	private int numberItemSaved = 0

	public NaiveCrawler(Queue<CrawlerUrl> urlQueue, int maxNumberUrls,
	int maxDepth, long delayBetweenUrls, String regexpSearchPattern) {
		this.urlQueue = urlQueue
		this.maxNumberUrls = maxNumberUrls
		this.delayBetweenUrls = delayBetweenUrls
		this.maxDepth = maxDepth

		this.regexpSearchPattern = Pattern.compile(regexpSearchPattern)
		this.visitedUrls = new HashMap<>()
		this.sitePermissions = new HashMap<>()

		this.httpRegexp = Pattern.compile(REGEXP_HTTP)
		this.relativeRegexp = Pattern.compile(REGEXP_RELATIVE)
		this.crawlOutput = new BufferedWriter(new FileWriter("crawl.txt"))
		this.crawlStatistics = new BufferedWriter(new FileWriter(
				"crawlStatistics.txt"))
		this.crawlIgnoreOutput = new BufferedWriter(
				new FileWriter("crawlIgnore.txt"))
	}
	public void crawl() {
		while (continueCrawling()) {
			CrawlerUrl url = getNextUrl()
			if (url != null) {
				printCrawlInfo()
				String content = getContent(url)
				if (isContentRelevant(content, this.regexpSearchPattern)) {
					saveContent(url)
					Collection<String> urlStrings = extractUrls(content, url)
					addUrlsToUrlQueue(url, urlStrings)
				} else {
					println "$url is not relevant ignoring..."
					crawlIgnoreOutput.append(url.getUrlString() + "\n")
				}
				sleep(this.delayBetweenUrls)
			}
		}
		closeOutputStream()
	}

	// 크롤링 조건 임계치를 달성할때까지 크롤링
	private boolean continueCrawling() {
		return (!urlQueue.isEmpty()) &&
				(getNumberOfUrlsVisited() <= this.maxNumberUrls)
	}
	// 다음 방문할 URL 받기
	private CrawlerUrl getNextUrl() {
		CrawlerUrl nextUrl = null
		while ((nextUrl == null) && (!urlQueue.isEmpty())) {
			CrawlerUrl crawlerUrl = this.urlQueue.remove()
			if (doWeHavePermissionToVisit(crawlerUrl) &&
			(!isUrlAlreadyVisited(crawlerUrl)) && isDepthAcceptable(crawlerUrl)) {
				nextUrl = crawlerUrl
			}
		}
		return nextUrl
	}
	// 크롤러의 상세 상황 출력
	private void printCrawlInfo() {
		StringBuilder sb =new StringBuilder()
		sb.append("queue length=").append(this.urlQueue.size())
				.append(" visited url=").append(getNumberOfUrlsVisited())
				.append(" site permissions=").append(this.sitePermissions.size())
		crawlStatistics.flush()
		println sb.toString()
	}
	// 방문한 URL의 개수를 리턴
	private int getNumberOfUrlsVisited() {
		return this.visitedUrls.size()
	}

	// 모든 출력 스트림 닫기
	private void closeOutputStream() {
		crawlOutput.flush()
		crawlOutput.close()
		crawlStatistics.flush()
		crawlStatistics.close()
		crawlIgnoreOutput.flush()
		crawlIgnoreOutput.close()
	}

	//방문 허용 여부를 체크하는 robots.txt 파일 파싱
	private boolean doWeHavePermissionToVisit(CrawlerUrl crawlerUrl) {
		if (crawlerUrl == null) {
			return false
		}
		if (!crawlerUrl.isCheckedForPermission()) {
			crawlerUrl.setAllowedToVisit(true)
			//			crawlerUrl.setAllowedToVisit(computePermissionForVisiting(crawlerUrl))
		}
		return crawlerUrl.isAllowedToVisit()
	}
	// 이미 방문한 URL인지 확인
	private boolean isUrlAlreadyVisited(CrawlerUrl crawlerUrl) {
		if (crawlerUrl.isVisited() ||
		(this.visitedUrls.containsKey(crawlerUrl.getUrlString()))) {
			return true
		}
		return false
	}
	//URL의 깊이 확인
	private boolean isDepthAcceptable(CrawlerUrl crawlerUrl) {
		return crawlerUrl.getDepth() <= this.maxDepth
	}
	private boolean computePermissionForVisiting(CrawlerUrl crawlerUrl) {
		URL url = crawlerUrl.getURL()
		boolean retValue = (url != null)
		if (retValue) {
			String host = url.getHost() // 사이트 접근 권한은 지역 변수에 저장
			Collection<String> disallowedPaths = this.sitePermissions.get(host)
			if (disallowedPaths == null) {
				disallowedPaths = parseRobotsTextFileToGetDisallowedPaths(host) // 파일을 파싱
			}
			String path = url.getPath() // 허용되지 않는 경로인지 확인
			for (String disallowedPath : disallowedPaths) {
				if (path.contains(disallowedPath)) {
					retValue = false
				}
			}
		}
		return retValue
	}
	private Collection<String> parseRobotsTextFileToGetDisallowedPaths(String host) {
		String robotFilePath = getContent("http://" + host + "/robots.txt")
		Collection<String> disallowedPaths = new ArrayList<>()
		if (robotFilePath != null) {
			// 정규식 패턴 매칭을 통해 허용되지 않는 경로를 추출
			// 즉 User-agent:*에 해당하는 disallow path를 찾아낸다
			Pattern p = Pattern.compile(USER_AGENT)
			String[] permissionSets = p.split(robotFilePath)
			String permissionString = ""
			String permissionTrim = ""
			for (String permission : permissionSets) {
				permissionTrim = permission.replaceAll(" ", "")
				if (permissionTrim.startsWith("*")) {
					// 허용되지 않는 경로에서 * 문자열을 제거
					permissionString = permissionTrim.substring(1)
				}
			}
			p = Pattern.compile(DISALLOW)
			String[] items = p.split(permissionString)
			for (String s : items) {
				disallowedPaths.add(s.trim())
			}
		}
		this.sitePermissions.put(host, disallowedPaths)
		return disallowedPaths
	}
	private String getContent(String urlString) {
		return getContent(new CrawlerUrl(urlString, 0))
	}
	private String getContent(CrawlerUrl url) {
		HttpClient client = new DefaultHttpClient()
		// URL에 해당하는 컨텐츠를 가져옴
		HttpGet method = new HttpGet(url.getUrlString())

		String text = null
		try {
			HttpResponse response = client.execute(method)
			text = readContentsFromStream(new InputStreamReader(
					response.getEntity().getContent()))
		} catch (Exception e) {
			println e.toString()
			e.printStackTrace()
		} finally{
			method.releaseConnection()
		}
		markUrlAsVisited(url)
		return text
	}
	// 스트림을 문자열로 변환
	private static String readContentsFromStream(Reader input) {
		BufferedReader bufferedReader  = null
		if (input instanceof BufferedReader) {
			bufferedReader = (BufferedReader) input
		} else {
			bufferedReader = new BufferedReader(input)
		}
		StringBuilder sb = new StringBuilder()
		char[] buffer = new char[4 * 1024]
		int charsRead
		while ((charsRead = bufferedReader.read(buffer)) != -1) {
			sb.append(buffer, 0, charsRead)
		}
		return sb.toString()
	}

	// 컨첸츠를 가져왔다면 방문했다고 표시
	private void markUrlAsVisited(CrawlerUrl url) {
		this.visitedUrls.put(url.getUrlString(), url)
		url.setIsVisited()
	}

	public List<String> extractUrls(String text, CrawlerUrl crawlerUrl) {
		Map<String, String> urlMap = new HashMap<>()
		extractHttpUrls(urlMap, text)
		extractRelativeUrls(urlMap, text, crawlerUrl)
		return new ArrayList<String>(urlMap.keySet())
	}
	private void extractHttpUrls(Map<String, String> urlMap, String text) {
		Matcher m = httpRegexp.matcher(text)
		while (m.find()) {
			String url = m.group()
			String[] terms = url.split("a href=\"")
			for (String term : terms) {
				if (term.startsWith("http")) {
					int index = term.indexOf("\"")
					if (index > 0) {
						term = term.substring(0, index)
					}
					urlMap.put(term, term)
				}
			}
		}
	}
	private void extractRelativeUrls(Map<String, String> urlMap, String text,
			CrawlerUrl crawlerUrl) {
		Matcher m = relativeRegexp.matcher(text)
		URL textURL = crawlerUrl.getURL()
		String host = textURL.getHost()
		while (m.find()){
			// ,구분자로 이어진 일치된 url string
			String url = m.group()
			String[] terms = url.split("a href=\"")
			for (String term : terms) {
				// a href 태그중에서 url 값만을 잘라 낸다
				if (term.startsWith("/")) {
					// url에서 "으로 끝나는 위치를 찾아낸다
					int index = term.indexOf("\"")
					if (index > 0) {
						term = term.substring(0, index)
					}
					String s = "http://" + host + term
					urlMap.put(s, s)
				}
			}
		}
	}
	private void addUrlsToUrlQueue(CrawlerUrl url, Collection<String> urlStrings) {
		int depth = url.getDepth() + 1
		for (String urlString : urlStrings) {
			if (!this.visitedUrls.containsKey(urlString)) {
				this.urlQueue.add(new CrawlerUrl(urlString, depth))
			}
		}
	}
	//관련 컨텐츠인지 검사
	public boolean isContentRelevant(String content, Pattern regexpPattern) {
		boolean retValue = false
		if (content != null) {
			Matcher m = regexpPattern.matcher(content.toLowerCase())
			retValue = m.find()
		}
		return retValue
	}
	private void saveContent(CrawlerUrl url) {
		this.crawlOutput.append(url.getUrlString()).append("\n")
		numberItemSaved++
	}

	static main(args) {
		Queue<CrawlerUrl> urlQueue = new LinkedList<CrawlerUrl>()
		String url = "https://twitter.com"
		//		String url = "http://en.wikipedia.org/wiki/Collective_intelligence"
		//		String url = "http://ko.wikipedia.org/wiki/%EC%A7%91%EB%8B%A8_%EC%A7%80%EC%84%B1"
		//		String regexp = "collective.*intelligence"
		//		String regexp = "집단지성"
		String regexp = "하둡"
		urlQueue.add(new CrawlerUrl(url, 0))

		NaiveCrawler crawler = new NaiveCrawler(urlQueue, 100, 5, 10L,
				regexp)
		crawler.crawl()
	}
}