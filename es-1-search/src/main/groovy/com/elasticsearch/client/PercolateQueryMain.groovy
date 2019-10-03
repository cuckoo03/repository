package com.elasticsearch.client

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.XContentFactory
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
	}
	static main(args) {
		def main = new PercolateQueryMain()
		main.createClient()
		main.test()
	}
}
