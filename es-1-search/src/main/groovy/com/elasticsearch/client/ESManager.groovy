package com.elasticsearch.client

import java.util.List

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.FilterBuilders
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHits
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram.Interval
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Order
import com.elasticsearch.util.DateUtil
import groovy.transform.TypeChecked

@TypeChecked
class ESManager {
	private final String CLUSTER_NAME_FIELD = "cluster.name"
	private final String CLUSTER_NAME = "elasticsearch"
	private final String ELASTIC_SEARCH_IP = "es.ip"
	private final int ELASTIC_SEARCH_PORT = 9300
	private final String TYPE_NAME = "article"
	
	private Client client
	private void createClient() {
		def s = ImmutableSettings.settingsBuilder()
		.put(CLUSTER_NAME_FIELD, CLUSTER_NAME)
		.build();
		def tmp = new TransportClient(s);
		tmp.addTransportAddress(new InetSocketTransportAddress(
			ELASTIC_SEARCH_IP, ELASTIC_SEARCH_PORT));
		client = tmp;
	}
	def void init() {
		createClient()
	}
	
	/**
	 * search document
	 * @param collection 채널명
	 * @param from
	 * @param to
	 * @param offset
	 * @param size
	 */
	def SearchHits searchDocument(String collection, String searchQuery, String from, String to, 
		int offset, int size) {
		def indexNames = makeIndexNames(collection, from, to)
		def matchQury = QueryBuilders.matchQuery("body", searchQuery)
		def response = client.prepareSearch(indexNames as String[])
			.setTypes(TYPE_NAME)
			.addFields("title").addFields("body").addFields("article_id").addFields("create_date")
			.setQuery(matchQury)
			.setFrom(offset).setSize(size)
			.execute().actionGet()
		
		return response.hits
	}
	/**
	 * 
	 * @param collection
	 * @param searchQuery
	 * @param from yyyyMMdd
	 * @param to yyyyMMdd
	 */
	def DateHistogram searchTrend(String collection, String searchQuery, String from, String to) {
		def indexNames = makeIndexNames(collection, from, to)
		final def AGG_NAME = "dateHistogram"
		def filter1 = FilterBuilders.termFilter("body", searchQuery)
		def filter2 = FilterBuilders.rangeFilter("createDate")
			.from(from + "000000").to(to + "000000")
		def filterBuilder = FilterBuilders.andFilter(filter1, filter2)
		def qb = QueryBuilders.constantScoreQuery(filterBuilder)
		
		def dateHistogramBuilder = AggregationBuilders.dateHistogram(AGG_NAME)
			.interval(Interval.HOUR).order(Order.COUNT_DESC).field("createDate")
		def response = client.prepareSearch(indexNames as String[])
			.setTypes(TYPE_NAME).setQuery(qb).addAggregation(dateHistogramBuilder)
			.execute().actionGet()
			
		return response.aggregations.get(AGG_NAME) as DateHistogram
	}
	def void searchTopic(String collection, String searchQuery, String from, 
		String to, int offset, int size) {
	}
	/**
	 * 기간에 해당하는 인덱스명 리스트를 생성한다.
	 * @param channel twitter
	 * @param from yyyyMMdd
	 * @param to yyyyMMdd
	 */
	def List<String> makeIndexNames(String channel, String from, String to) {
		def list = 	DateUtil.getDaysFromAToB(from, to)
		def collected = list.collectAll({it ->
			channel + "-" + it
		})
		return collected.reverse()
	}
}
