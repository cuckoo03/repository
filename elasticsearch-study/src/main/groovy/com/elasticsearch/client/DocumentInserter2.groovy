package com.elasticsearch.client

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.json.JsonXContent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.elasticsearch.client.dao.TapacrossDao
import com.elasticsearch.client.entity.TableEntity

import groovy.transform.TypeChecked

@TypeChecked
class DocumentInserter2 {
	private def start
	Client client
	final String INDEX_NAME1 = "twitter1712"
	final String TYPE_NAME1 = "mytype"
	final String FIELD1_NAME = "articleId"
	final String FIELD2_NAME = "title"
	final String FIELD3_NAME = "body"
	final String FIELD4_NAME = "createDate"
	
	private ApplicationContext context;
	
	@Autowired
	private TapacrossDao dao

	void run() {
		this.context = new GenericXmlApplicationContext(
			"classpath:spring/application-context.xml");
		dao = context.getBean(TapacrossDao.class)
		
		createClient()
		
//		deleteIndex(INDEX_NAME1)
//		createIndex(INDEX_NAME1)
//		createMapping(INDEX_NAME1, TYPE_NAME1)
		
		def start = System.currentTimeMillis()
		println "start add documents"
		
		createThreads()
		
		sleep(1000 * 60 * 60 * 24)
//		addDocument("tb_article_search_twitter_1711", INDEX_NAME1, TYPE_NAME1)
		println "end add document. elasped:${(System.currentTimeMillis() - start) / 1000}s."
	}
	
	int sequence = 1
	void createThreads() {
		12.times {
			Thread.start {
				while (true) {
					addDocument("tb_article_search_twitter_1712", INDEX_NAME1, TYPE_NAME1)
				}
			}
		}
	}
	int makeSequence() {
		def fetch = 10000
		def end = sequence + fetch
		sequence = end
		
		return end
		
	}
	void createClient() {
		def s = Settings.settingsBuilder()
				.put("cluster.name", "elasticsearch").build();
		def tmp = TransportClient.builder().settings(s).build();
		tmp.addTransportAddress(
			new InetSocketTransportAddress(
				new InetSocketAddress("121.254.177.193", 9300)));
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
		if (indexExists(indexName)) {
			client.admin().indices().prepareDelete(indexName).execute().actionGet()
			client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet()
		}
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
				startObject().field(typeName)
					.startObject().field("properties")
						.startObject()
							.field(FIELD1_NAME)
								.startObject().field("type", "long")
								.endObject()
								.field(FIELD2_NAME)
									.startObject().field("type", "string")
									.endObject()
								.field(FIELD3_NAME)
									.startObject().field("type", "string")
									.endObject()
								.field(FIELD4_NAME)
									.startObject().field("type", "date").field("format", "yyyyyMMddHHmmss")
									.endObject()
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
		def end = 0
		synchronized(this) {
			start = sequence
			end = makeSequence()
		}
		def dest = 180000000
		if (start >= dest) {
			println "dest:$start"
			System.exit(1)
		}
		
		if (start < dest) {
			def startTime = System.currentTimeMillis()
			def bulker = client.prepareBulk()
			def result = dao.selectArticles(start, end, tableName)
			result.each { TableEntity it ->
				def currentIndexName = "twitter" + it.createDate.substring(2, 8)
				def ir = client.prepareIndex(indexName, typeName, it.seq.toString())
				.setSource(FIELD1_NAME, it.articleId,
					FIELD2_NAME, "", FIELD3_NAME, it.body,
					FIELD4_NAME, it.createDate)
				bulker.add(ir)
			}
			bulker.execute().actionGet()
			
			def elasped = (System.currentTimeMillis() - startTime) / 1000
			println "add $tableName, start:$start, end:$end, elapsed:$elasped"
//			start = end + 1
//			end = end + fetch
		}
	}
	static void updateDocument() {
		//		client.prepareUpdate(INDEX_NAME, TYPE_NAME, "1").setScript(ScriptService.ScriptType.INLINE)
	}
	
	static void main(args) {
		def insert = new DocumentInserter2()
		insert.run()
	}
}
