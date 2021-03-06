package com.elasticsearch.client

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.cluster.metadata.MappingMetaData
import org.elasticsearch.common.collect.ImmutableOpenMap
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.json.JsonXContent
import org.elasticsearch.index.query.QueryBuilders

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.transform.TypeChecked
import java.text.SimpleDateFormat

/**
 * https://www.programcreek.com/java-api-examples/?api=org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse
 * @author jkko
 *
 */
@TypeChecked
class ManageIndexMain {
	private Client client
	
//	private final String TABLE_NAME = "tb_article_search_twitter_1908"
	private static final String INDEX_NAME = "media-20200201"
	private static final String TYPE_NAME = "article"
	private final String FIELD1_NAME = "articleId"
	private final String FIELD2_NAME = "title"
	private final String FIELD3_NAME = "body"
	private final String FIELD4_NAME = "createDate"
	private final String ELASTIC_SEARCH_IP = "broker.ip"
	private final int ELASTIC_SEARCH_NATIVE_PORT = 9300
	private final int ELASTIC_SEARCH_REST_PORT = 9200
	private final String CLUSTER_NAME_FIELD = "cluster.name"
	private final String CLUSTER_NAME = "tapa-es"
	private final String PROPERTIES_FIELD_NAME = "properties"
	private final String TYPE_FIELD_NAME = "type"
	private final String FORMAT_FIELD_NAME = "format"

	private final String LONG_FIELD_TYPE = "long"
	private final String STRING_FIELD_TYPE = "string"
	private final String DATE_FIELD_TYPE = "date"
	
	private final String FORMAT_FIELD_VALUE = "yyyyyMMddHHmmss"
	
	void createClient() {
		def s = ImmutableSettings.settingsBuilder()
				.put(CLUSTER_NAME_FIELD, CLUSTER_NAME)
				.build();
		def tmp = new TransportClient(s);
		tmp.addTransportAddress(new InetSocketTransportAddress(
			ELASTIC_SEARCH_IP, ELASTIC_SEARCH_NATIVE_PORT));
		client = tmp;
	}
	void createIndex(String indexName) {
		if (!indexExists(indexName)) {
			client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet()
			println "$indexName:index create success."
		} else {
			println "exist index."
		}
	}
	void createIndexRest(String indexName) {
		if (indexExists(indexName)) {
			println "exist index."
			return
		}
		
		def data = 
		"""
{
"settings" : {
    "article": {
        "analysis": {
            "analyzer": {
                "my_analyzer": {
                    "type": "custom", "tokenizer": "my_tokenizer", "filter":"my_filter"
                },
                "topic_analyzer": {
                    "type": "custom", "tokenizer": "topic_tokenizer", "filter":"topic_filter"
                },
                "sentiment_analyzer": {
                    "type": "custom", "tokenizer": "sentiment_tokenizer", "filter":"sentiment_filter"
                },
                "occasion_analyzer": {
                    "type": "custom", "tokenizer": "occasion_tokenizer", "filter":"occasion_filter"
                }
            }
        }
      },
      "number_of_shards" : 5,
      "number_of_replicas" : 1
	  },
        "mappings":{
            "article": {
                "_all":{"enabled":false},
                "dynamic":"false",
                "properties": {
                    "article_id": {"type":"string", "index":"not_analyzed"},
                    "content_id": {"type":"string", "index":"not_analyzed"},
                    "site_id": {"type":"string", "index":"not_analyzed"},
                    "writer_id": {"type":"string", "index":"not_analyzed"},
                    "title": {"type":"string", "analyzer":"my_analyzer"},
                    "body": {"type":"string", "analyzer":"my_analyzer"},
                    "rt": {"type":"string", "index":"not_analyzed"},
                    "reply_id": {"type":"string", "index":"not_analyzed"},
                    "reply_writer_id": {"type":"string", "index":"not_analyzed"},
                    "re": {"type":"string", "index":"not_analyzed"},
                    "address": {"type":"string", "index":"not_analyzed"},
                    "address2": {"type":"string", "index":"not_analyzed"},
                    "create_date": {"type":"date", "format":"yyyyMMddHHmmss"},
                    "site_type": {"type":"string", "index":"not_analyzed"},
                    "via_url": {"type":"string", "index":"not_analyzed"},
					"url": {"type":"string", "index":"not_analyzed"},
                    "rt_count": {"type":"long"},
                    "follower_count": {"type":"long"},
                    "site_name": {"type":"string", "index":"not_analyzed"},
                    "picture": {"type":"string", "index":"not_analyzed"},
                    "screen_name": {"type":"string", "index":"not_analyzed"},
                    "site_category": {"type":"string", "index":"not_analyzed"},
                    "site_sub_category": {"type":"string", "index":"not_analyzed"},
                    "hit_count": {"type":"long"},
                    "comment_count": {"type":"long"},
                    "like_count": {"type":"long"},
                    "polarity": {"type":"long"},
                    "topic": {"type": "string", "analyzer":"topic_analyzer"},
                    "sentiment": {"type": "string", "analyzer":"sentiment_analyzer"},
                    "occasion": {"type": "string", "analyzer":"occasion_analyzer"}
                    }
                }
            }
          }
}
"""
		def json = new JsonSlurper().parseText(data)
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(
			"http://$ELASTIC_SEARCH_IP:$ELASTIC_SEARCH_REST_PORT/$indexName");

		StringEntity input = new StringEntity(data);
		input.setContentType("application/text");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			println response
			throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatusLine().getStatusCode());
		}

		BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));

		String output;
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		httpClient.getConnectionManager().shutdown();
	}
	def void createDailyIndexes() {
		def channel = "media"
		def indexName = ""
		def cal = Calendar.instance
		cal.set(Calendar.YEAR, 2020)
		cal.set(Calendar.MONTH, 1)
		cal.set(Calendar.DATE, 1)
		def sdf = new SimpleDateFormat("yyyyMMdd")
		while (cal.get(Calendar.DATE) < 2) {
			def formatted = sdf.format(cal.time)
			deleteIndex("$channel$formatted")
			createIndexRest("$channel-$formatted")
			cal.add(Calendar.DATE, + 1)
		}
	}
	boolean indexExists(String indexName) {
		def map = client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getIndices()
		return map.containsKey(indexName)
	}
	void deleteIndex(String indexName) {
		if (indexExists(indexName)) {
			client.admin().indices().prepareDelete(indexName).execute().actionGet()
			println "$indexName index delete success."
		} else {
			println "$indexName index not found."
		}
	}
	void putMapping(String indexName, String typeName) {
		// mapping info
		// properties:{
		// 	field1:{
		//		type:string
		// }, field2:{
		// 		type:string
		// }
		//}
		def builder = JsonXContent.contentBuilder().
				startObject().field(typeName)
					.startObject().field(PROPERTIES_FIELD_NAME)
						.startObject()
							.field(FIELD1_NAME)
								.startObject().field(TYPE_FIELD_NAME, LONG_FIELD_TYPE)
								.endObject()
								.field(FIELD2_NAME)
									.startObject().field(TYPE_FIELD_NAME, STRING_FIELD_TYPE)
									.endObject()
								.field(FIELD3_NAME)
									.startObject().field(TYPE_FIELD_NAME, STRING_FIELD_TYPE)
									.endObject()
								.field(FIELD4_NAME)
									.startObject().field(TYPE_FIELD_NAME, DATE_FIELD_TYPE).field(FORMAT_FIELD_NAME, FORMAT_FIELD_VALUE)
									.endObject()
						.endObject()
					.endObject()
				.endObject()
		def response = client.admin().indices().preparePutMapping(indexName)
				.setType(typeName).setSource(builder).execute().actionGet()
		if (!response.acknowledged) {
			println "something strange happens"
		} else {
			println "put mapping success."
		}
	}
	def void showMapping(String indexName, String typeName) {
		def request = new GetMappingsRequest()
		def response = client.admin().indices().
			prepareGetMappings(indexName).setTypes(typeName).get() as GetMappingsResponse
	
		ImmutableOpenMap<String, MappingMetaData> mappings = response.getMappings().get(indexName);
		MappingMetaData mappingMetaData = mappings.get(typeName);
	
		Map<String, Object> mappingSource = mappingMetaData.getSourceAsMap();
	
		def fieldNames = [FIELD1_NAME, FIELD2_NAME, FIELD3_NAME, FIELD4_NAME]
		for (String fieldName : fieldNames) {
			def mappingProperties = (Map<String, Object>) mappingSource.get("properties");
			println "$fieldName:${mappingProperties[fieldName]}"
		}
	}
	def void showMappingRest(String indexName) {
		def data = "http://$ELASTIC_SEARCH_IP:$ELASTIC_SEARCH_REST_PORT/$INDEX_NAME/_mapping?pretty"
		println data
		println new URL(data).getText()
	}
	def void showSettingsRest(String indexName) {
		def data = "http://$ELASTIC_SEARCH_IP:$ELASTIC_SEARCH_REST_PORT/$INDEX_NAME/_settings?pretty"
		println data
		println new URL(data).getText()
	}
	def void showClusterStateRest() {
		def data = "http://$ELASTIC_SEARCH_IP:$ELASTIC_SEARCH_REST_PORT/_cluster/state?pretty"
		println new URL(data).getText()
	}
	def void analyze() {
		def request = new AnalyzeRequest("좋은 #트와이스 #멜론 #멜론이벤트 트와이스 필스페셜 너무좋다".toLowerCase())
			.index(INDEX_NAME)
			.analyzer("my_analyzer")
		def tokens = client.admin().indices().analyze(request).actionGet().getTokens();
		for (AnalyzeResponse.AnalyzeToken token : tokens) {
			println token.term + " " + token.startOffset + "->" + token.endOffset +
				" type:" + token.type + " pos:" + token.position
		}
	}
	def void createPercolate(String indexName, String percolatorName) {
		if (!indexExists(indexName)) {
			println "not exist index."
			return
		}
		
		def data =
		"""
{
		"query":{
			"match":{
				"body":"트와이스"
			}
		}
}
"""
		def json = new JsonSlurper().parseText(data)
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(
			"http://$ELASTIC_SEARCH_IP:$ELASTIC_SEARCH_REST_PORT/$indexName/.percolator/$percolatorName");

		StringEntity input = new StringEntity(data);
		input.setContentType("application/text");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);
		if (response.getStatusLine().getStatusCode() != 200) {
			println response
			throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatusLine().getStatusCode());
		}

		BufferedReader br = new BufferedReader(
						new InputStreamReader((response.getEntity().getContent())));

		String output;
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		httpClient.getConnectionManager().shutdown();
	}
	def void deletePercolator(String indexName, String percolatorName) {
		
	}
	def void deleteDocuments(String indexName, String typeName) {
		def response = client.prepareDeleteByQuery(indexName)
			.setTypes(typeName)
			.setQuery(QueryBuilders.matchAllQuery())
			.execute().actionGet()
		println response
	}
	def void showTermVectorRest(String indexName, String typeName, String id) {
		def data = "http://$ELASTIC_SEARCH_IP:$ELASTIC_SEARCH_REST_PORT/$indexName/$typeName/$id/_termvector?fields=body&pretty=true"
		println data
		def result = new URL(data).getText()
		
		def json = new JsonSlurper().parseText(result)
		if (!result.contains("body"))
			return
		
		json["term_vectors"]["body"]["terms"].each {
			def keySize = (it["key"] as String).size()
			if (keySize > 10) {
				println "docId:$id, term:${it['key']}"
//				showDocumentRest(indexName, typeName, id)
				sleep(500)
			}
		}
	}
	def void showTermVectorsRest(String indexName, String typeName) {
		1.step(1000, 1, {
			showTermVectorRest(indexName, typeName, it.toString())
		})
	}
	def void showDocumentRest(String indexName, String typeName, String id) {
		def data = "http://$ELASTIC_SEARCH_IP:$ELASTIC_SEARCH_REST_PORT/$indexName/$typeName/$id/?fields=body&pretty=true"
		println data
		def result = new URL(data).getText()
		println result
	}
	static void main(args) {
		def main = new ManageIndexMain()
		main.createClient()
//		main.deleteIndex(INDEX_NAME)
//		main.createIndex(INDEX_NAME)

//		main.createIndexRest(INDEX_NAME)
//		main.putMapping(INDEX_NAME1, TYPE_NAME1)
//		main.showMapping(INDEX_NAME1, TYPE_NAME1)

//		main.showMappingRest(INDEX_NAME1)
//		main.showSettingsRest(INDEX_NAME1)

//		main.showClusterStateRest()
//		main.analyze()
		
		main.createDailyIndexes()
//		main.deletePercolator(INDEX_NAME, "p1")
//		main.createPercolate(INDEX_NAME, "p1")
		
//		main.crateDocument()
//		main.deleteDocuments(INDEX_NAME, TYPE_NAME)
//		main.showTermVectorRest(INDEX_NAME, TYPE_NAME, "1")
		main.showTermVectorsRest(INDEX_NAME, TYPE_NAME)
//		main.showDocumentRest(INDEX_NAME, TYPE_NAME, "1")
	}
}
