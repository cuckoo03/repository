package com.elasticsearch.client

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.QueryBuilders

import groovy.transform.TypeChecked

@TypeChecked
class PercolateQueryMain {
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
		def final highlightField = "body"
		def termQuery = QueryBuilders.matchQuery("body", "트와이스멜론")
		def response = client.prepareSearch(INDEX_NAMES as String[])
		.setTypes(TYPE_NAME).setQuery(termQuery).addHighlightedField(highlightField)
		.setHighlighterFragmentSize(4)
		.setHighlighterNumOfFragments(3)
		.setHighlighterPreTags("<b>")
		.setHighlighterPostTags("</b>")
		.setHighlighterEncoder("html")
		.execute().actionGet()
		if (response.status().status == 200) {
			println "matched number:${response.getHits().getTotalHits()}"
			println "score:$response.hits.maxScore"
			for( def hit : response.hits) {
				println hit.index + ":" + hit.type + ":" + hit.id + ":" + hit.field("articleId") + ":" + hit.source
				def highlights = hit.highlightFields()
				def highlight = highlights[highlightField]
				if (highlight) {
					println "highlight field=$highlight.name size:${highlight.fragments.size()} $highlight.fragments"
					highlight.fragments.each({println it})
				}
			}
		}
	}
	static main(args) {
		def main = new PercolateQueryMain()
		main.createClient()
		main.test()
	}
}
