package com.elasticsearch.client

import java.text.SimpleDateFormat
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

import groovy.transform.TypeChecked

@TypeChecked
class DocumentInserter {
	private def start
	private Client client
	private final int FETCH_SIZE = 100
	private final String TABLE_NAME = "tb_article_search_twitter_1901"
	private final String INDEX_NAME = "twitter-20190101"
	private final String TYPE_NAME = "article"
	private final String ELASTIC_SEARCH_IP = "es.ip"
	private final int ELASTIC_SEARCH_PORT = 9300
	private final String CLUSTER_NAME_FIELD = "cluster.name"
	private final String CLUSTER_NAME = "elasticsearch"
	private final String PROPERTIES_FIELD_NAME = "properties"
	private final String TYPE_FIELD_NAME = "type"
//	private final String FORMAT_FIELD_NAME = "format"

//	private final String LONG_FIELD_TYPE = "long"
//	private final String STRING_FIELD_TYPE = "string"
//	private final String DATE_FIELD_TYPE = "date"
	
//	private final String FORMAT_FIELD_VALUE = "yyyyyMMddHHmmss"
	
	private ApplicationContext context;
	
	@Autowired
	private TapacrossDao dao

	void run() {
		this.context = new GenericXmlApplicationContext(
			"classpath:spring/application-context.xml");
		dao = context.getBean(TapacrossDao.class)
		createClient()
		
		def start = System.currentTimeMillis()
		println "start add documents"
		
		createThreads()
//		final def content = "좋은 #트와이스 #멜론 #멜론이벤트 트와이스 필스페셜 너무좋다ㅜㅜ 꼭 1위가쟈!!"
//		addDocument(INDEX_NAME, TYPE_NAME, 1, "1", "title1", content, 
//			"2019090400000", content, content, content)
		
		sleep(1000 * 60 * 60 * 24)
		println "end add document. elasped:${(System.currentTimeMillis() - start) / 1000}s."
	}
	
	int sequence = 1
	void createThreads() {
		1.times {
			Thread.start {
				while (true) {
					addBulkDocuments(TABLE_NAME, INDEX_NAME, TYPE_NAME)
				}
			}
		}
	}
	int makeSequence(int sequence) {
		def fetch = FETCH_SIZE
		def end = sequence + fetch
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
	void addBulkDocuments(String tableName, String indexName, String typeName) {
		def fetch = FETCH_SIZE
		def start = 0
		def end = 0
		synchronized(this) {
			start = sequence
			end = makeSequence(sequence)
			sequence = end
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
				def articleIndexName = "twitter-" + it.createDate.substring(0, 8)
				def ir = client.prepareIndex(articleIndexName, typeName, it.seq.toString())
				.setSource(
					TableField.FIELD1_NAME, it.articleId,
					TableField.FIELD2_NAME, it.title?.toLowerCase(), 
					TableField.FIELD3_NAME, it.body?.toLowerCase(),
					TableField.FIELD4_NAME, it.createDate,
					TableField.FIELD5_NAME, it.body?.toLowerCase(),
					TableField.FIELD6_NAME, it.body?.toLowerCase(),
					TableField.FIELD7_NAME, it.body?.toLowerCase(),
				)
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
		String createDate, String topic, String sentiment, String occasion) {
		def ir = client.prepareIndex(indexName, typeName, seq.toString())
				.setSource(
					TableField.FIELD1_NAME, articleId,
					TableField.FIELD2_NAME, title, 
					TableField.FIELD3_NAME, body,
					TableField.FIELD4_NAME, createDate,
					TableField.FIELD5_NAME, topic,
					TableField.FIELD6_NAME, sentiment,
					TableField.FIELD7_NAME, occasion
				).execute().actionGet()
		println "version=$ir.version"
		def gr = client.prepareGet(indexName, typeName, seq.toString()).execute().actionGet()
		println gr.source
	}
	static void main(args) {
		def insert = new DocumentInserter()
		insert.run()
	}
}
