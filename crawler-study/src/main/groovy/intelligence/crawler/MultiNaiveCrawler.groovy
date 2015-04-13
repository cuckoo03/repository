package intelligence.crawler

import groovy.transform.TypeChecked

import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@TypeChecked
class MultiNaiveCrawler {
	private int maxNumberUrls
	private long delayBetweenUrls
	private int maxDepth
	// 방문한 url의 목록을 유지, 중복허용하지 않음
	private Map<String, CrawlerUrl> visitedUrls
	// 크롤링 허가 여부를 관리
	private Map<String, Collection<String>> sitePermissions
	// 방문할 Url queue, 중복url을 허용하지 않음
	private BlockingQueue<CrawlerUrl> urlQueue
	private BufferedWriter crawlOutput
	private BufferedWriter crawlStatistics
	// 연관된 컨텐츠가 없는 url을 기록
	private BufferedWriter crawlIgnoreOutput
	// 크롤링된 url 출력 개수
	private AtomicInteger numberItemSaved = new AtomicInteger(0)

	private String regexpSearchPattern
	private ExecutorService executor

	public MultiNaiveCrawler(BlockingQueue<CrawlerUrl> urlQueue, int maxNumberUrls,
	int maxDepth, long delayBetweenUrls, String regexpSearchPattern, int threads){
		this.urlQueue = urlQueue
		this.maxNumberUrls = maxNumberUrls
		this.delayBetweenUrls = delayBetweenUrls
		this.maxDepth = maxDepth
		this.regexpSearchPattern = regexpSearchPattern
		this.visitedUrls = new ConcurrentHashMap<>()
		this.sitePermissions = new ConcurrentHashMap<>()
		this.crawlOutput = new BufferedWriter(new FileWriter("crawl.txt"))
		this.crawlStatistics = new BufferedWriter(new FileWriter(
				"crawlStatistics.txt"))
		this.crawlIgnoreOutput = new BufferedWriter(
				new FileWriter("crawlIgnore.txt"))

		executor = Executors.newFixedThreadPool(threads)
	}
	public void crawl() {
		executor.execute(
				new CrawlerThread(urlQueue, maxNumberUrls,
				visitedUrls, maxDepth, sitePermissions, crawlStatistics,
				crawlOutput, numberItemSaved, regexpSearchPattern,
				crawlIgnoreOutput, delayBetweenUrls))
		
		sleep(10000)
		println "shutdown"
		executor.shutdown()
		if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
			if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
				println "still running"
				executor.shutdownNow()
			}
		}
		closeOutputStream()
		
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

	static main(args) {
		BlockingQueue<CrawlerUrl> urlQueue = new LinkedBlockingQueue<CrawlerUrl>()
		String url = "http://en.wikipedia.org/wiki/Collective_intelligence"
		//		String url = "http://ko.wikipedia.org/wiki/%EC%A7%91%EB%8B%A8_%EC%A7%80%EC%84%B1"
		String regexp = "collective.*intelligence"
		//		String regexp = "집단지성"
		urlQueue.add(new CrawlerUrl(url, 0))

		MultiNaiveCrawler crawler = new MultiNaiveCrawler(urlQueue, 10, 5, 10L,
				regexp, 4)
		crawler.crawl()
	}
}