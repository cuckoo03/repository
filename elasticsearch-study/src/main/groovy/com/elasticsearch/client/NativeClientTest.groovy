package com.elasticsearch.client

import groovy.transform.TypeChecked

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.json.JsonXContent

@TypeChecked
class NativeClientTest {
	static Client client
	static final String INDEX_NAME = "myindex"
	static final String TYPE_NAME = "mytype"
	static final String FIELD1_NAME = "field1"
	static final String FIELD2_NAME = "field2"

	static main(args) {
		createClient()
		createIndex()
		createMapping()
	}
	static void createClient() {
		def s = ImmutableSettings.settingsBuilder()
				.put("cluster.name", "elasticsearch").build();
		def tmp = new TransportClient(s);
		tmp.addTransportAddress(new InetSocketTransportAddress("210.122.10.31",
				9300));
		client = tmp;
	}
	static void createIndex() {
		if (!indexExists(INDEX_NAME)) {
			client.admin().indices().create(new CreateIndexRequest(INDEX_NAME)).actionGet()
		} else {
			deleteIndex(INDEX_NAME)
			client.admin().indices().create(new CreateIndexRequest(INDEX_NAME)).actionGet()
		}
		createMapping()
		addDocument()
//		bulkOperation()
//		deleteIndex(INDEX_NAME)
	}
	static boolean indexExists(String indexName) {
		def map = client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getIndices()
		return map.containsKey(indexName)
	}

	static void deleteIndex(String indexName) {
		client.admin().indices().prepareDelete(indexName).execute().actionGet()
	}
	static void createMapping() {
		// properties:{
		// 	field1:{
		//		type:string
		// }, field2:{
		// 		type:string
		// }
		//}
		def builder = JsonXContent.contentBuilder().
				startObject().field("")
					.startObject().field("properties")
						.startObject()
							.field(FIELD1_NAME)
								.startObject().field("type", "string")
								.field("tokenizer", "whitespace")
								.endObject()
							.field(FIELD2_NAME)
								.startObject().field("type", "string")
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
	static void addDocument() {
		(1..10).each { 
			def id = String.valueOf(it)
			def value = String.valueOf(it)
			def ir = client.prepareIndex(INDEX_NAME, TYPE_NAME, id)
					.setSource(FIELD1_NAME, value + " 필드", FIELD2_NAME, value + "값")
					.execute().actionGet()
//			def urb = client.prepareUpdate(INDEX_NAME, TYPE_NAME, id)
//				.setUpsert(ir)
//			def updateResponse = urb.get()
		}
	}
	static void updateDocument() {
//		client.prepareUpdate(INDEX_NAME, TYPE_NAME, "1").setScript(ScriptService.ScriptType.INLINE)
	}
	static void bulkOperation() {
		def bulker = client.prepareBulk()
		(1..700).each {
			def id = String.valueOf(it)
			def value = String.valueOf(it)
			def ir = client.prepareIndex(INDEX_NAME, TYPE_NAME, id + "12345678910")
					.setSource(FIELD1_NAME, value, FIELD2_NAME, value+ "12345678910")
			bulker.add(ir)
		}
		bulker.execute().actionGet()
	}
}
