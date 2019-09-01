package com.elasticsearch.client

import java.util.Collections;
import java.util.List;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.json.JsonXContent
import org.elasticsearch.plugins.Plugin
import org.elasticsearch.node.Node
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.node.internal.InternalSettingsPreparer;
import org.elasticsearch.transport.Netty4Plugin
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.elasticsearch.PluginNode
import com.elasticsearch.client.dao.TapacrossDao
import com.elasticsearch.client.entity.TableEntity

import groovy.transform.TypeChecked

/**
 * 순차적으로 과거 월 색인을 만드는 프로그램
 * @author admin
 *
 */
@TypeChecked
class DocumentBulkInserter {
	private def start
	private Client client
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
		
		createTransportClient()
//		createNativeClient()
//		NativeClient()
		
		def start = System.currentTimeMillis()
		println "start add documents"
		
		createThreads("tb_article_search_twitter_1702")
	}
	
	private static class PluginNode extends Node {
		public PluginNode(Settings preparedSettings,
				List<Class<? extends Plugin>> plugins) {
			super(InternalSettingsPreparer.prepareEnvironment(preparedSettings,
					null), plugins);
		}
	}
	
	public NativeClient() throws NodeValidationException {
		final Settings settings = Settings.builder().put("path.home", "/tmp")
				.put("client.transport.sniff", true)
				.put("cluster.name", "elasticsearch").put("node.data", false)
				.put("node.master", false).put("node.ingest", false).build();

		node = new PluginNode(
				settings,
				Collections
						.<Class<? extends Plugin>> singletonList(Netty4Plugin.class));
		node.start();
		client = node.client();
	}
	
	void createTransportClient() {
		def settings = Settings.builder()
				.put("cluster.name", "elasticsearch")
				.put("client.transport.sniff", true)
				.put("node.data", false)
				.put("node.master", true)
				.put("node.ingest", false)
				.build();
		def tmp = new PreBuiltTransportClient(settings)
		tmp.addTransportAddress(
			new InetSocketTransportAddress(
				new InetSocketAddress("210.122.10.31", 9300)));
		client = (Client) tmp;
	}
	
	private Node node
	void createNativeClient() {
		final Settings settings = Settings.builder()
				.put("path.home", "/tmp")
				.put("client.transport.sniff", true)
				.put("cluster.name", "elasticsearch")
				.put("node.data", false)
				.put("node.master", false)
				.put("node.ingest", false).build()

		node = new PluginNode(settings, Collections.<Class<? extends Plugin>>singletonList(Netty4Plugin.class))
		node.start()
		client = node.client()
	}
	
	int sequence = 1
	void createThreads(String tableName) {
		1.times {
			Thread.start {
				while (true) {
					try {
						addDocument(tableName, 
							tableName.replace("tb_article_search_", ""), TYPE_NAME1)
					} catch (Exception e) {
						e.printStackTrace()
						sleep(5 * 1000)
					}
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
		def dest = 150000000
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
					FIELD2_NAME, "", FIELD3_NAME, it.body,
					FIELD4_NAME, it.createDate)
				bulker.add(ir)
			}
			try {
				bulker.execute().actionGet()
			} catch (Exception e) {
				e.printStackTrace()
				sleep(3 * 1000)
			}
			
			def elasped = (System.currentTimeMillis() - startTime) / 1000
			println "add $tableName, start:$start, end:$end, elapsed:$elasped"
		}
	}
	static void updateDocument() {
		//		client.prepareUpdate(INDEX_NAME, TYPE_NAME, "1").setScript(ScriptService.ScriptType.INLINE)
	}
	
	static void main(args) {
		def insert = new DocumentBulkInserter()
		insert.run()
	}
}
