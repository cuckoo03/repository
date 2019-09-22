package com.elasticsearch.client
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;
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
	
	private final String INDEX_NAME1 = "twitter_1908"
	private final String ELASTIC_SEARCH_IP = "es.ip"
	private final int ELASTIC_SEARCH_PORT = 9300
	private final String CLUSTER_NAME_FIELD = "cluster.name"
	private final String CLUSTER_NAME = "elasticsearch"
	private final String TYPE_NAME1 = "post"
	def void createClient() {
		def s = ImmutableSettings.settingsBuilder()
				.put(CLUSTER_NAME_FIELD, CLUSTER_NAME)
				.build();
		def tmp = new TransportClient(s);
		tmp.addTransportAddress(new InetSocketTransportAddress(
			ELASTIC_SEARCH_IP, ELASTIC_SEARCH_PORT));
		client = tmp;
	}
	def void aggregate() {
		def termQuery = QueryBuilders.termQuery("body", "나중")
		def aggsBuilder = terms("bodyTerms").field("body")
		def extStatsAggsBuider = extendedStats("createDateStats").field("createDate")
		def response = client.prepareSearch(INDEX_NAME1).
			setTypes(TYPE_NAME1).addFields("articleId", "body").
			setQuery(termQuery).
			addAggregation(aggsBuilder).addAggregation(extStatsAggsBuider).
			execute().actionGet()
		if (response.status().status == 200) {
			println "matched number:${response.getHits().getTotalHits()}"
			def termsAggs = response.aggregations.get("bodyTerms") as Terms
			println termsAggs.name + ":" + termsAggs.buckets.size()
			for (def bucket : termsAggs.buckets) {
				println "-" + bucket.key + ", " + bucket.docCount
			}
			
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
	def void execute() {
//		def response = client.prepareSearch(INDEX_NAME1).
//		setTypes(TYPE_NAME1).addFields("articleId", "body").setQuery(query).execute().actionGet()
	} 
	static void main(args) {
		def main = new AggregationQueryMain()
		main.createClient()
		main.aggregate()
		
	}
}
