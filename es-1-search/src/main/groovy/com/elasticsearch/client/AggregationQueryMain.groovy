package com.elasticsearch.client
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.FilterBuilders
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram.Interval
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramBuilder
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Order
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram
import org.elasticsearch.search.aggregations.bucket.terms.Terms
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsAggregator
import org.elasticsearch.search.internal.InternalSearchHit
import groovy.transform.TypeChecked
import java.text.SimpleDateFormat

/**
 * https://www.programcreek.com/java-api-examples/?api=org.elasticsearch.search.aggregations.AggregationBuilders
 * @author admin
 *
 */
@TypeChecked
class AggregationQueryMain {
	private Client client
	
	private final String INDEX_NAME = "twitter-20190101"
	private final List<String> INDEX_NAMES = ["twitter-20190101", "twitter-20190101"]
	private final String ELASTIC_SEARCH_IP = "es.ip"
	private final int ELASTIC_SEARCH_PORT = 9300
	private final String CLUSTER_NAME_FIELD = "cluster.name"
	private final String CLUSTER_NAME = "elasticsearch"
	private final String TYPE_NAME = "article"
	def void createClient() {
		def s = ImmutableSettings.settingsBuilder()
				.put(CLUSTER_NAME_FIELD, CLUSTER_NAME)
				.build();
		def tmp = new TransportClient(s);
		tmp.addTransportAddress(new InetSocketTransportAddress(
			ELASTIC_SEARCH_IP, ELASTIC_SEARCH_PORT));
		client = tmp;
	}
	def void termAggregate() {
		final def AGG_NAME = "terms"
		final def STATS_AGG_NAME = "createDateStats" 
		def termQuery = QueryBuilders.matchQuery("body", "RT")
		def aggsBuilder = AggregationBuilders.terms(AGG_NAME).field("sentiment") as TermsBuilder
		aggsBuilder.size(4)
		def extStatsAggsBuider = AggregationBuilders.extendedStats(STATS_AGG_NAME).field("createDate")
		// SearchRequestBuilder에 addField를 추가한경우 리턴되는 source 객체는 널을 리턴한다
		def response = client.prepareSearch(INDEX_NAMES as String[])
		.setTypes(TYPE_NAME).setQuery(termQuery).addAggregation(aggsBuilder)
		.addAggregation(extStatsAggsBuider)
		.execute().actionGet()
			
		if (response.status().status == 200) {
			println "matched number:${response.getHits().getTotalHits()}"
			def termsAggs = response.aggregations.get(AGG_NAME) as Terms
			println termsAggs.name + ":" + termsAggs.buckets.size()
			for (def bucket : termsAggs.buckets) {
				println bucket.key + " " + bucket.docCount + " "
			}
			
			def extStats = response.aggregations.get(STATS_AGG_NAME) as ExtendedStats
			println extStats.name
			println "count:$extStats.count"
			println "min:$extStats.min"
			println "max:$extStats.max"
			println "standard deviation:$extStats.stdDeviation"
			println "sum of sequares:$extStats.sumOfSquares"
			println "variance:$extStats.variance"
		}
	}
	def void range() {
		AggregationBuilders.range("")
	}
	def void dateRange() {
		AggregationBuilders.dateRange("")
	}
	def void a() {
		AggregationBuilders.cardinality("")//add es1.1
		AggregationBuilders.percentiles("")//add es1.1
		AggregationBuilders.significantTerms("")// add es1.1
		AggregationBuilders.nested("")//add es1.0

	}
	// add es1.3
	def void tophitAggregate() {
		final def AGG_NAME = "terms"
		final def TOPHIT_AGG_NAME = "tophit"
		def termQuery = QueryBuilders.matchQuery("body", "트와이스 멜론")
		def aggsBuilder = AggregationBuilders.terms(AGG_NAME).field("body")
		def tophitBuilder = AggregationBuilders.topHits(TOPHIT_AGG_NAME)
			.setExplain(true).setFrom(0).setSize(1)
		// SearchRequestBuilder에 addField를 추가한경우 리턴되는 source 객체는 널을 리턴한다
		def response = client.prepareSearch(INDEX_NAME)
		.setTypes(TYPE_NAME).setQuery(termQuery)
		.addAggregation(aggsBuilder)
		.addAggregation(tophitBuilder)
		.setSize(1)
		.execute().actionGet()
			
		if (response.status().status == 200) {
			println "matched number:${response.getHits().getTotalHits()}"
			def termsAggs = response.aggregations.get(AGG_NAME) as Terms
			println termsAggs.name + ":" + termsAggs.buckets.size()
			for (def bucket : termsAggs.buckets) {
				println bucket.key + " " + bucket.docCount + " "
			}
			
			def tophitAggs = 
				response.aggregations.get(TOPHIT_AGG_NAME) as TopHits
			println tophitAggs.hits
			tophitAggs.hits.forEach({ InternalSearchHit it ->
				println it.source
			})
		}
	}
	// add es1.4
	def void filterAggregate() {
		
	}
	// add es1.4
	def void childrenAggregate() {
		
	}
	def void histogramAggregate() {
		final def AGG_NAME = "histogram" 
		def filter1 = FilterBuilders.termFilter("body", "트와이스")
		def filter2 = FilterBuilders.rangeFilter("articleId")
		.from("1").to("3")
		def filterBuilder = FilterBuilders.andFilter(filter1, filter2)
		def qb = QueryBuilders.constantScoreQuery(filterBuilder)
		def histogramBuilder = AggregationBuilders.histogram(AGG_NAME)
		.interval(1).order(Order.KEY_DESC).field("articleId")
		def response = client.prepareSearch(INDEX_NAME)
		.setTypes(TYPE_NAME).setQuery(qb).addAggregation(histogramBuilder)
		.execute().actionGet()
		
		if (response.status().status == 200) {
			def histogramAggs = response.aggregations.get(AGG_NAME) as Histogram
			println histogramAggs.name + ":" + histogramAggs.buckets.size()
			histogramAggs.buckets.each({ 
				def bucket = it as InternalHistogram.Bucket
				println "key:${bucket.getKey()}, value:${bucket.getDocCount()}"
			})
		}
	}
	def void dateHistogramAggregation() {
		final def AGG_NAME = "dateHistogram"
		def filter1 = FilterBuilders.termFilter("body", "트와이스")
		def filter2 = FilterBuilders.rangeFilter("createDate")
		.from("20190903").to("20190905000000")
		def filterBuilder = FilterBuilders.andFilter(filter1, filter2)
		def qb = QueryBuilders.constantScoreQuery(filterBuilder)
		
		def dateHistogramBuilder = AggregationBuilders.dateHistogram(AGG_NAME)
			.interval(Interval.HOUR).order(Order.COUNT_DESC).field("createDate")
		def response = client.prepareSearch(INDEX_NAME)
		.setTypes(TYPE_NAME).setQuery(qb).addAggregation(dateHistogramBuilder)
		.execute().actionGet()
		
		if (response.status().status == 200) {
			def dateHistoagramAggs = response.aggregations.get(AGG_NAME) as DateHistogram
			println dateHistoagramAggs.name + ":" + dateHistoagramAggs.buckets.size()
			dateHistoagramAggs.buckets.each({ 
				def bucket = it as InternalHistogram.Bucket
				println "key:${bucket.getKey()}, value:${bucket.getDocCount()}"
//				println bucket.getProperties()
			})
		}
	}
	def List<String> createDailyIndexes() {
		def result = []
		def indexName = ""
		def cal = Calendar.instance
		cal.set(Calendar.YEAR, 2019)
		cal.set(Calendar.MONTH, 0)
		cal.set(Calendar.DATE, 1)
		def sdf = new SimpleDateFormat("yyyyMMdd")
		while (cal.get(Calendar.YEAR) > 2010) {
			def formatted = sdf.format(cal.time)
			result += "twitter-$formatted"
			cal.add(Calendar.DATE, - 1)
		}
		
		return result
	}
	static void main(args) {
		def main = new AggregationQueryMain()
		main.createClient()
		main.termAggregate()
		println ""
//		main.histogramAggregate()
		println ""
//		main.dateHistogramAggregation()
		println ""
//		main.tophitAggregate()
	}
}
