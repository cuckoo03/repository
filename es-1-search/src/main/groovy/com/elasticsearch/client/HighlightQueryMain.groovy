package com.elasticsearch.client

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.Terms
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats
import org.elasticsearch.search.highlight.HighlightField

import groovy.transform.TypeChecked

/**
 * https://www.programcreek.com/java-api-examples/?api=org.elasticsearch.search.aggregations.AggregationBuilders
 * @author admin
 *
 */
@TypeChecked
class HighlightQueryMain {
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
	def void test() {
		def termQuery = QueryBuilders.matchQuery("body", "트와이스 멜론")
		def response = client.prepareSearch(INDEX_NAMES as String[])
		.setTypes(TYPE_NAME).setQuery(termQuery).addHighlightedField("body")
		.execute().actionGet()
		if (response.status().status == 200) {
			println "matched number:${response.getHits().getTotalHits()}"
			if (response.status().getStatus() == 200) {
				println response.hits.totalHits
				println response.hits.maxScore
				for( def hit : response.hits) {
					println hit.index + ":" + hit.type + ":" + hit.id + ":" + hit.field("articleId") + ":" + hit.source
					def highlights = hit.highlightFields()
					highlights.each { k, v ->					
						println k["fragments"]
					}
				}
			}
		}
	}
	static main(args) {
		def main = new HighlightQueryMain()
		main.createClient()
		main.test()
	}
}