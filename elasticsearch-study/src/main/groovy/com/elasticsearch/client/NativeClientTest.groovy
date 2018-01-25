package com.elasticsearch.client

import static org.elasticsearch.index.query.FilterBuilders.*
import static org.elasticsearch.index.query.QueryBuilders.*
import static org.elasticsearch.search.aggregations.AggregationBuilders.*

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.json.JsonXContent
import org.elasticsearch.index.query.FilterBuilders
import org.elasticsearch.index.query.MatchAllFilterBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.aggregations.bucket.terms.Terms
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats

import groovy.transform.TypeChecked

@TypeChecked
class NativeClientTest {
	Client client
	final String INDEX_NAME = "myindex2"
	final String TYPE_NAME = "mytype"
	final String FIELD1_NAME = "articleId"
	final String FIELD3_NAME = "body"
	final String FIELD4_NAME = "createDate"

	def void run() {
		createClient()
//		deleteIndex(INDEX_NAME)
//		createIndex(INDEX_NAME)
//		createMapping()
//		addDocument()
		bulkOperation()
		search()
		search2()
//		aggregations()
	}
	
	static main(args) {
		def c = new NativeClientTest()
		c.run()	
	}
	void createClient() {
		def s = ImmutableSettings.settingsBuilder()
				.put("cluster.name", "elasticsearch").build();
		def tmp = new TransportClient(s);
		tmp.addTransportAddress(new InetSocketTransportAddress("210.122.10.31",
				9300));
		client = tmp;
	}
	void createIndex(String indexName) {
		if (!indexExists(indexName)) {
			client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet()
		}
	}
	void deleteIndex(String indexName) {
		if (indexExists(indexName)) {
			client.admin().indices().prepareDelete(indexName).execute().actionGet()
			client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet()
		}
	}
	boolean indexExists(String indexName) {
		def map = client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getIndices()
		return map.containsKey(indexName)
	}

	void createMapping() {
		// properties:{
		// 	field1:{
		//		type:string
		// }, field2:{
		// 		type:string
		// }
		//}
		def builder = JsonXContent.contentBuilder().
				startObject().field(TYPE_NAME)
				.startObject().field("properties")
				.startObject()
				.field(FIELD3_NAME)
				.startObject().field("type", "string")
				.endObject()
				.field(FIELD1_NAME)
				.startObject().field("type", "long")
				.endObject()
				.field(FIELD4_NAME)
				.startObject().field("type", "date")
				.field("format", "yyyyyMMddHHmmss")
				.endObject()
				.endObject()
				.endObject()
				.endObject()
		def response = client.admin().indices().preparePutMapping(INDEX_NAME)
				.setType(TYPE_NAME).setSource(builder).execute().actionGet()
		if (!response.acknowledged) {
			println "something strange happens"
		}
	}
	void addDocument() {
		(1..10).each {
			def id = String.valueOf(it)
			def value = String.valueOf(it)
			def ir = client.prepareIndex(INDEX_NAME, TYPE_NAME, id)
					.setSource(FIELD1_NAME, id, FIELD3_NAME, "아버지가방에들어가신다")
					.execute().actionGet()
		}
	}
	void bulkOperation() {
		def bulker = client.prepareBulk()
		(1..10).each {
			def id = String.valueOf(it)
			def value = String.valueOf(it)
			def ir = client.prepareIndex(INDEX_NAME, TYPE_NAME, id + "")
					.setSource(FIELD1_NAME, value, 
						FIELD3_NAME, "전주비빕밥")
			bulker.add(ir)
		}
		bulker.execute().actionGet()
	}
	void search() {
		def filter = FilterBuilders.termFilter(FIELD3_NAME, "전주비빕밥")
//		def matchFilter = FilterBuilders.matchAllFilter()
		def range = QueryBuilders.rangeQuery(FIELD4_NAME).gt(20171201000001)
		def bool = QueryBuilders.boolQuery().must(range)
		def query = QueryBuilders.filteredQuery(bool, filter)
		def response = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).
		setQuery(query).execute().actionGet()
		println "search:" + response.getHits().getTotalHits()
	}
	void search2() {
		def query = QueryBuilders.matchQuery(FIELD3_NAME, "전주비빕밥")
//		filteredQuery(boolQuery().must(
//			rangeQuery(FIELD2_NAME).gt(20171201000001)), termFilter(FIELD2_NAME, "밤"))	
		def response = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).
		setQuery(query).execute().actionGet()
		println "search2:" + response.getHits().getTotalHits()
		response.getHits().getHits().each { SearchHit it -> 
			println "hit:$it.index, $it.type, $it.id"
		}
		println "status:" + response.status().getStatus()
	}
	void aggregations() {
		def aggsBuilder = terms("aggs1").field(FIELD3_NAME)
		def aggsBuilder2 = extendedStats("aggs2").field(FIELD3_NAME)
		def query = QueryBuilders.matchQuery(FIELD3_NAME, "전주")
		def response = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).
			setQuery(query).addAggregation(aggsBuilder).
			addAggregation(aggsBuilder2).execute().actionGet()
		if (response.status().status == 200) {
			println "matched number of documents:${response.hits.totalHits}"

			def termsAggs = response.aggregations.get("aggs1") as Terms
			println "aggregation name:$termsAggs.name"
			println "aggregation bucket size:${termsAggs.buckets.size()}"
			termsAggs.buckets.each {
				println "$it.key, $it.docCount"
			}
			
			def extStats = response.getAggregations().get("aggs2") as ExtendedStats
			println "aggregation name:$extStats.name"
			println "aggregation count:$extStats.count"
			println "aggregation min:$extStats.min"
			println "aggregation max:$extStats.max"
			println "aggregation Standard Deviation:$extStats.stdDeviation"
			println "aggregation sum of squares:$extStats.sumOfSquares"
			println "aggregation variance:$extStats.variance"
		}
	}
}
