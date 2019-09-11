package com.elasticsearch.client

import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.junit.Test

import groovy.transform.TypeChecked

@TypeChecked
class DocumentAnalyzeTest {
	private Client client
	
	private final String TABLE_NAME = "tb_article_search_twitter_1908"
	private final String INDEX_NAME1 = "twitter_1908"
	private final String TYPE_NAME1 = "mytype"
	private final String FIELD1_NAME = "articleId"
	private final String FIELD2_NAME = "title"
	private final String FIELD3_NAME = "body"
	private final String FIELD4_NAME = "createDate"
	private final String ELASTIC_SEARCH_IP = "220.230.124.211"
	private final int ELASTIC_SEARCH_PORT = 9300
	private final String CLUSTER_NAME_FIELD = "cluster.name"
	private final String CLUSTER_NAME = "elasticsearch"
	private final String PROPERTIES_FIELD_NAME = "properties"
	private final String TYPE_FIELD_NAME = "type"
	private final String FORMAT_FIELD_NAME = "format"

	private final String LONG_FIELD_TYPE = "long"
	private final String STRING_FIELD_TYPE = "string"
	private final String DATE_FIELD_TYPE = "date"
	
	private final String FORMAT_FIELD_VALUE = "yyyyyMMddHHmmss"
	
	@Test
	void createClient() {
		def s = ImmutableSettings.settingsBuilder()
				.put(CLUSTER_NAME_FIELD, CLUSTER_NAME)
				.build();
		def tmp = new TransportClient(s);
		tmp.addTransportAddress(new InetSocketTransportAddress(
			ELASTIC_SEARCH_IP, ELASTIC_SEARCH_PORT));
		client = tmp;
		
		AnalyzeRequest request = (new AnalyzeRequest("방탄소년단")).index(INDEX_NAME1)
			.analyzer("my_analyzer")
			.tokenizer("my_tokenizer");//my_analyzer
		List<AnalyzeResponse.AnalyzeToken> tokens = client.admin().indices()
			.analyze(request).actionGet().getTokens();
		for (AnalyzeResponse.AnalyzeToken token : tokens)
		{
			println token.term + " " + token.startOffset + "->" + token.endOffset + " " + token.type
		}
	}
}
