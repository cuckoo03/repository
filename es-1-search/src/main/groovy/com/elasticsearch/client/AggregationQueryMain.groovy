package com.elasticsearch.client
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;

import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.aggregations.bucket.terms.Terms
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats
import groovy.transform.TypeChecked

@TypeChecked
class AggregationQueryMain {
	private Client client
	
	private final String INDEX_NAME = "twitter_1908"
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
		def termQuery = QueryBuilders.matchQuery("body", "트와이스 멜론")
		def aggsBuilder = terms("bodyTerms").field("body")
		def extStatsAggsBuider = extendedStats("createDateStats").field("createDate")
		// SearchRequestBuilder에 addField를 추가한경우 리턴되는 source 객체는 널을 리턴한다
		def response = client.prepareSearch(INDEX_NAME).
			setTypes(TYPE_NAME).
			setQuery(termQuery).
			addAggregation(aggsBuilder).
			addAggregation(extStatsAggsBuider).
			execute().actionGet()
			
			printResponse(response)
			printStats(response)
	}
	def void histogramAggregate() {
	}
	def void dateHistogramAggregation() {
	}
	def void printResponse(SearchResponse response) {
		if (response.status().status == 200) {
			println "matched number:${response.getHits().getTotalHits()}"
			def termsAggs = response.aggregations.get("bodyTerms") as Terms
			println termsAggs.name + ":" + termsAggs.buckets.size()
			for (def bucket : termsAggs.buckets) {
				println bucket.key + " " + bucket.docCount + " "
			}
		}
	}
	def void printStats(SearchResponse response) {
		if (response.status().status == 200) {
			def extStats = response.aggregations.get("createDateStats") as ExtendedStats
			println extStats.name
			println "count:$extStats.count"
			println "min:$extStats.min"
			println "max:$extStats.max"
			println "standard deviation:$extStats.stdDeviation"
			println "sum of sequares:$extStats.sumOfSquares"
			println "variance:$extStats.variance"
		}
	}
	static void main(args) {
		def main = new AggregationQueryMain()
		main.createClient()
		main.termAggregate()
	}
}
