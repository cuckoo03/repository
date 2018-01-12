package com.elasticsearch.client

import groovy.transform.TypeChecked

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.json.JsonXContent
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.elasticsearch.client.dao.TapacrossDao
import com.elasticsearch.client.entity.TableEntity

@TypeChecked
class DocumentInserter {
	Client client
	final String INDEX_NAME1 = "twitter"
	final String INDEX_NAME2 = "media"
	final String TYPE_NAME1 = "1711"
	final String TYPE_NAME2 = "1710"
	final String FIELD1_NAME = "articleId"
	final String FIELD2_NAME = "title"
	final String FIELD3_NAME = "body"
	final String FIELD4_NAME = "createDate"
	final String FIELD5_NAME = "siteType"
	
	private ApplicationContext context;
	
	@Autowired
	private TapacrossDao dao


	void run() {
		this.context = new GenericXmlApplicationContext(
			"classpath:spring/application-context.xml");
		dao = context.getBean(TapacrossDao.class)
		
		createClient()
		deleteIndex(INDEX_NAME1)
		deleteIndex(INDEX_NAME2)
		createIndex(INDEX_NAME1)
		createIndex(INDEX_NAME2)
		createMapping(INDEX_NAME1, TYPE_NAME1)
		createMapping(INDEX_NAME2, TYPE_NAME1)
		
		def start = System.currentTimeMillis()
		println "start add documents"
		addDocument("tb_article_search_twitter_1711", INDEX_NAME1, TYPE_NAME1)
//		addDocument("tb_article_search_twitter_1710", INDEX_NAME1, TYPE_NAME2)
//		addDocument("tb_article_search_media_1711", INDEX_NAME2, TYPE_NAME1)
//		addDocument("tb_article_search_media_1710", INDEX_NAME2, TYPE_NAME2)
		println "end add document. elasped:${(System.currentTimeMillis() - start) / 1000}s."
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
		} else {
			
		}
	}
	boolean indexExists(String indexName) {
		def map = client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getIndices()
		return map.containsKey(indexName)
	}

	void deleteIndex(String indexName) {
		client.admin().indices().prepareDelete(indexName).execute().actionGet()
		client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet()
	}
	void createMapping(String indexName, String typeName) {
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
				.startObject().field("type", "long")
				.endObject()
				.field(FIELD2_NAME)
				.startObject().field("type", "string").field("tokenizer", "whitespace")
				.endObject()
				.field(FIELD3_NAME)
				.startObject().field("type", "string").field("tokenizer", "whitespace")
				.endObject()
				.field(FIELD4_NAME)
				.startObject().field("type", "date").field("format", "yyyyMMddHHmmss")
				.endObject()
				.field(FIELD5_NAME)
				.startObject().field("type", "string")
				.endObject()
				.endObject()
				.endObject()
		def response = client.admin().indices().preparePutMapping(indexName)
				.setType(typeName).setSource(builder).execute().actionGet()
		if (!response.acknowledged) {
			println "something strange happens"
		}
	}
	
	void addDocument(String tableName, String indexName, String typeName) {
		def fetch = 10000
		def start = 0
		def end = start + fetch
		def dest = 100000000
		while (start < dest) {
			def startTime = System.currentTimeMillis()
			def bulker = client.prepareBulk()
			def result = dao.selectArticles(start, end, tableName)
			result.each { TableEntity it ->
				def ir = client.prepareIndex(indexName, typeName, it.seq.toString())
				.setSource(FIELD1_NAME, it.articleId,
					FIELD2_NAME, it.title, FIELD3_NAME, it.body,
					FIELD4_NAME, it.createDate, FIELD5_NAME, it.siteType)
				bulker.add(ir)
			}
			bulker.execute().actionGet()
			
			def elasped = (System.currentTimeMillis() - startTime) / 1000
			println "add $tableName, start:$start, end:$end, elapsed:$elasped"
			start = end + 1
			end = end + fetch
		}
	}
	static void updateDocument() {
		//		client.prepareUpdate(INDEX_NAME, TYPE_NAME, "1").setScript(ScriptService.ScriptType.INLINE)
	}
	
	static void main(args) {
		def insert = new DocumentInserter()
		insert.run()
	}
}
