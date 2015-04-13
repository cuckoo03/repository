package intelligence.crawler

import groovy.transform.TypeChecked

import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.LinkedBlockingQueue

@TypeChecked
class MultiNaiveCrawler {
	private int maxNumberUrls
	private long delayBetweenUrls
	private int maxDepth
	// 방문한 url의 목록을 유지, 중복허용하지 않음
	private ConcurrentHashMap<String, CrawlerUrl> visitedUrls
	// 크롤링 허가 여부를 관리
	private ConcurrentHashMap<String, Collection<String>> sitePermissions
	// 방문할 Url queue, 중복url을 허용하지 않음
	private BlockingQueue<CrawlerUrl> urlQueue
	private BufferedWriter crawlOutput
	private BufferedWriter crawlStatistics
	// 연관된 컨텐츠가 없는 url을 기록
	private BufferedWriter crawlIgnoreOutput
	// 크롤링된 url 출력 개수
	private AtomicInteger numberItemSaved

	private String regexpSearchPattern
	private ExecutorService executor
	private int threads = 0

	public MultiNaiveCrawler(BlockingQueue<CrawlerUrl> urlQueue, int maxNumberUrls,
	int maxDepth, long delayBetweenUrls, String regexpSearchPattern, int threads){
		this.urlQueue = urlQueue
		this.maxNumberUrls = maxNumberUrls
		this.delayBetweenUrls = delayBetweenUrls
		this.maxDepth = maxDepth
		this.regexpSearchPattern = regexpSearchPattern
		this.visitedUrls = new ConcurrentHashMap<String, CrawlerUrl>()
		this.sitePermissions = new ConcurrentHashMap<String, Collection<String>>()
		this.crawlOutput = new BufferedWriter(new FileWriter("crawl.txt"))
		this.crawlStatistics = new BufferedWriter(new FileWriter(
				"crawlStatistics.txt"))
		this.crawlIgnoreOutput = new BufferedWriter(
				new FileWriter("crawlIgnore.txt"))
		this.numberItemSaved = new AtomicInteger(0)
		executor = Executors.newFixedThreadPool(threads)
		this.threads = threads
	}
	public void crawl() {
		for (int i = 0; i < threads; i++) {
			executor.execute(
					new CrawlerThread(urlQueue, maxNumberUrls,
					visitedUrls, maxDepth, sitePermissions, crawlStatistics,
					crawlOutput, numberItemSaved, regexpSearchPattern,
					crawlIgnoreOutput, delayBetweenUrls))
		}

		sleep(60000)
		println "shutdown."
		executor.shutdown()
		if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
			if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
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
		println "start"
		BlockingQueue<CrawlerUrl> urlQueue = new LinkedBlockingQueue<CrawlerUrl>()
		String url = "http://en.wikipedia.org/wiki/Collective_intelligence"
		//		String url = "http://ko.wikipedia.org/wiki/%EC%A7%91%EB%8B%A8_%EC%A7%80%EC%84%B1"
		String regexp = "collective.*intelligence"
		//		String regexp = "집단지성"
		urlQueue.add(new CrawlerUrl(url, 0))

		MultiNaiveCrawler crawler = new MultiNaiveCrawler(urlQueue, 1000, 5, 10L,
				regexp, 4)
		crawler.crawl()
		println "end"
	}
}