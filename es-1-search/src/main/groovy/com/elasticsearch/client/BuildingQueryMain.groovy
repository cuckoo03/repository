package com.elasticsearch.client

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.FilterBuilders
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders

import groovy.transform.TypeChecked

@TypeChecked
class BuildingQueryMain {
	private Client client
	
	private final String INDEX_NAME = "twitter-20190101"
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
	def void termQuery() {
		def termQuery = QueryBuilders.termQuery("body", "rt")
		executeQuery(termQuery)
	}
	def void termsQuery() {
		def termQuery = QueryBuilders.termsQuery("body", "좋아해", "주자")
		executeQuery(termQuery)
	}
	def void rangeQuery() {
		def rangeQuery = rangeQuery("articleId").gt(1334793397663186000).
		includeLower(true).includeUpper(false);
		executeQuery(rangeQuery)
	}
	// not test
	def void boolQuery() {
		def termQuery = QueryBuilders.termQuery("body", "좋아해")
		def termQuery2 = QueryBuilders.termQuery("body", "주자")
		def bool = boolQuery().should(termQuery)
		executeQuery(bool)
	}
	def void matchQuery() {
		def matchQury = QueryBuilders.matchQuery("body", "RT")
		executeQuery(matchQury)
	}
	def void matchPhraseQuery() {
		def phraseQuery = QueryBuilders.matchPhraseQuery("body", "좋아해 주자")
		executeQuery(phraseQuery)
	}
	def void matchAllQuery() {
		def query = QueryBuilders.matchAllQuery()
		executeQuery(query)
	}
	def void andFilterQuery() {
		def termFilter = FilterBuilders.termFilter("body", "좋아해")
		def termFilter2 = FilterBuilders.termFilter("body", "주자")
		def filterBuilder = FilterBuilders.andFilter(termFilter, termFilter2)
		def qb = QueryBuilders.constantScoreQuery(filterBuilder)
		executeQuery(qb)
	}
	def void termFilterQuery() {
		def termQuery = QueryBuilders.termQuery("body", "나중")
		def termFilter = FilterBuilders.termFilter("body", "마음")
		def query = filteredQuery(termQuery, termFilter)
		executeQuery(query)
	}
	def void boolFilterQuery() {
		def termQuery = QueryBuilders.termQuery("body", "나중")
		def termFilter = FilterBuilders.termFilter("body", "마음")
		def boolFileder = FilterBuilders.boolFilter().must(termFilter)
		def query = filteredQuery(termQuery, boolFileder)
		executeQuery(query)
	}
	def void rangeFilterQuery() {
		def rangeFilter = FilterBuilders.rangeFilter("articleId").gt(1334793397663186000).
		includeLower(true).includeUpper(false)
		def qb = QueryBuilders.constantScoreQuery(rangeFilter)
		executeQuery(qb)
	}
	def void executeQuery(QueryBuilder query) {
		def response = client.prepareSearch(INDEX_NAME).
			setTypes(TYPE_NAME).addFields("body").setQuery(query).execute().actionGet()
			// SearchRequestBuilder에 addField를 추가한경우 리턴되는 source 객체는 널을 리턴한다
		if (response.status().getStatus() == 200) {
			println response.hits.totalHits
			println response.hits.maxScore
			for( def hit : response.hits) {
				println hit.index + ":" + hit.type + ":" + hit.id + ":" + hit.field("articleId") + ":" + hit.source
				println hit.field("body").value
			}
		}
	}
	static void main(args) {
		def main = new BuildingQueryMain()
		main.createClient()
//		main.termQuery()
//		main.termsQuery()
//		main.rangeQuery()
//		main.boolQuery()
		main.matchQuery()
//		main.matchPhraseQuery()
//		main.matchAllQuery()
//		main.andFilterQuery()
//		main.filteredTermQuery()
//		main.boolFilterQuery()
//		main.rangeFilterQuery()
	}
}