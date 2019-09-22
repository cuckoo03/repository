package com.elasticsearch.client

import java.util.concurrent.atomic.AtomicInteger

import javax.sql.rowset.Joinable
import javax.swing.plaf.IconUIResource

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.json.JsonXContent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.elasticsearch.client.dao.TapacrossDao
import com.elasticsearch.client.entity.TableEntity
import com.tapacross.sns.analyzer.MyAnalyzer2

import groovy.transform.TypeChecked

@TypeChecked
class DocumentInserter {
	private def start
	private Client client
	
	private final String TABLE_NAME = "tb_article_search_twitter_1908"
	private final String INDEX_NAME1 = "twitter_1908"
	private final String TYPE_NAME1 = "article"
	private final String FIELD1_NAME = "articleId"
	private final String FIELD2_NAME = "title"
	private final String FIELD3_NAME = "body"
	private final String FIELD4_NAME = "createDate"
	private final String ELASTIC_SEARCH_IP = "es.ip"
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
		
//		addDocument(INDEX_NAME1, TYPE_NAME1, 1, "2", "title1", "방탄소년단", "2019090300000")
		
		sleep(1000 * 60 * 60 * 24)
		println "end add document. elasped:${(System.currentTimeMillis() - start) / 1000}s."
	}
	
	int sequence = 1
	void createThreads() {
		1.times {
			Thread.start {
				while (true) {
					addDocuments(TABLE_NAME, INDEX_NAME1, TYPE_NAME1)
					sleep(1000)
				}
			}
		}
	}
	int makeSequence() {
		def fetch = fetch
		def end = sequence + fetch
		sequence = end
		return end
		
	}
	void createClient() {
		def s = ImmutableSettings.settingsBuilder()
				.put(CLUSTER_NAME_FIELD, CLUSTER_NAME)
				.build();
		def tmp = new TransportClient(s);
		tmp.addTransportAddress(new InetSocketTransportAddress(
			ELASTIC_SEARCH_IP, ELASTIC_SEARCH_PORT));
		client = tmp;
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
		}
	}
	
	def int fetch = 10
	void addDocuments(String tableName, String indexName, String typeName) {
		def fetch = fetch
		def start = 0
		def end = 0
		synchronized(this) {
			start = sequence
			end = makeSequence()
		}
		def dest = 100000000
		if (start >= dest) {
			println "dest:$start"
			System.exit(1)
		}
		
		if (start < dest) {
			def startTime = System.currentTimeMillis()
			def bulker = client.prepareBulk()
			def result = dao.selectArticles(start, end, tableName)
			result.each { TableEntity it ->
				def ir = client.prepareIndex(indexName, typeName, it.seq.toString())
				.setSource(FIELD1_NAME, it.articleId,
					FIELD2_NAME, it.title, FIELD3_NAME, it.body,
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
	
	void addDocument(String indexName, String typeName, int seq, String articleId, String title, String body, 
		String createDate) {
		def ir = client.prepareIndex(indexName, typeName, seq.toString())
				.setSource(FIELD1_NAME, articleId,
					FIELD2_NAME, title, 
					FIELD3_NAME, body,
					FIELD4_NAME, createDate).execute().actionGet()
		println "version=$ir.version"
		def gr = client.prepareGet(indexName, typeName, seq.toString()).execute().actionGet()
		println gr.source
	}
	
	static void updateDocument() {
		//		client.prepareUpdate(INDEX_NAME, TYPE_NAME, "1").setScript(ScriptService.ScriptType.INLINE)
	}
	
	static void main(args) {
		def insert = new DocumentInserter()
		insert.run()
	}
}
