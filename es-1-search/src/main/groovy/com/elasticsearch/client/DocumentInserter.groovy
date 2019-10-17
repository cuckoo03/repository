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
	private long jobStart
	private Client client
	private final int FETCH_SIZE = 1000
	private final String TABLE_NAME = "tb_article_search"
	private final String TYPE_NAME = "article"
	private final String ELASTIC_SEARCH_IP = "broker.ip"
	private final int ELASTIC_SEARCH_PORT = 9300
	private final String CLUSTER_NAME_FIELD = "cluster.name"
	private final String CLUSTER_NAME = "elasticsearch"
	private final String PROPERTIES_FIELD_NAME = "properties"
	private final String TYPE_FIELD_NAME = "type"
	private ApplicationContext context;
	int sequence = 0
	private String searchDate
	private String channel
	
	@Autowired
	private TapacrossDao dao

	void run(List<String> args) {
		if (args.size() == 0) {
			println "invalid args. <startRowSeq> <channel> <searchDate>. exit."
			System.exit(0)
		}
		sequence = args[0].toInteger()
		channel = args[1]
		searchDate = args[2]
		
		this.context = new GenericXmlApplicationContext(
			"classpath:spring/application-context.xml");
		dao = context.getBean(TapacrossDao.class)
		createClient()
		
		jobStart = System.currentTimeMillis()
		println "start add documents"
		
		createThreads()
		
		sleep(1000 * 60 * 60 * 24)
		println "end add document. elasped:${(System.currentTimeMillis() - jobStart) / 1000}s."
	}
	
	
	void createThreads() {
		4.times {
			Thread.start {
				while (true) {
					addBulkDocuments("${TABLE_NAME}_${channel}_$searchDate", TYPE_NAME)
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
	void addBulkDocuments(String tableName, String typeName) {
		def fetch = FETCH_SIZE
		def start = 0
		def end = 0
		synchronized(this) {
			start = sequence
			end = makeSequence(sequence)
			sequence = end
		}
		def dest = 200000000
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
					TableField.FIELD2_NAME, it.contentId, 
					TableField.FIELD3_NAME, it.siteId,
					TableField.FIELD4_NAME, it.writerId,
					TableField.FIELD5_NAME, it.title,
					TableField.FIELD6_NAME, it.body,
					TableField.FIELD7_NAME, it.rt,
					TableField.FIELD8_NAME, it.replyId,
					TableField.FIELD9_NAME, it.replyWriterId,
					TableField.FIELD10_NAME, it.re,
					TableField.FIELD11_NAME, it.address,
					TableField.FIELD12_NAME, it.address2,
					TableField.FIELD13_NAME, it.createDate,
					TableField.FIELD14_NAME, it.siteType,
					TableField.FIELD15_NAME, it.viaUrl,
					TableField.FIELD16_NAME, it.url,
					TableField.FIELD17_NAME, it.rtCount,
					TableField.FIELD18_NAME, it.followerCount,
					TableField.FIELD19_NAME, it.siteName,
					TableField.FIELD20_NAME, it.picture,
					TableField.FIELD21_NAME, it.screenName,
					TableField.FIELD22_NAME, it.siteCategory,
					TableField.FIELD23_NAME, it.siteSubType,
					TableField.FIELD24_NAME, it.hitCount,
					TableField.FIELD25_NAME, it.commentCount,
					TableField.FIELD26_NAME, it.likeCount,
					TableField.FIELD27_NAME, "C",
					TableField.FIELD28_NAME, 0,
					TableField.FIELD29_NAME, it.body,
					TableField.FIELD30_NAME, it.body,
					TableField.FIELD31_NAME, it.body 
				)
				bulker.add(ir)
			}
			bulker.execute().actionGet()
			
			def elasped = (System.currentTimeMillis() - startTime) / 1000
			println "add $tableName, start:$start, end:$end, elapsed:$elasped, tot elasped:${(System.currentTimeMillis() - jobStart) / 1000}"
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
		insert.run(args as List<String>)
	}
}
