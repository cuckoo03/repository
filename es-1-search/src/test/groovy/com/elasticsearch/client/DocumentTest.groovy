package com.elasticsearch.client

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.junit.Test

import groovy.transform.TypeChecked

@TypeChecked
class DocumentTest {
	private Client client
	private final String ELASTIC_SEARCH_IP = "broker.ip"
	private final int ELASTIC_SEARCH_PORT = 9300
	private final String CLUSTER_NAME_FIELD = "cluster.name"
	private final String CLUSTER_NAME = "tapa-es"
	
	private final String INDEX_NAME = "media-20200201"
	private final String TYPE = "article"
	
	void createClient() {
		def s = ImmutableSettings.settingsBuilder()
				.put(CLUSTER_NAME_FIELD, CLUSTER_NAME)
				.build();
		def tmp = new TransportClient(s);
		tmp.addTransportAddress(new InetSocketTransportAddress(
			ELASTIC_SEARCH_IP, ELASTIC_SEARCH_PORT));
		client = tmp;
	}
	
	@Test
	def void testInsert() {
		createClient()
		def title = "[카트 리그] 퍼스트A, 에이스 결정전까지 가는 접전 끝에 첫승 기록"
		def body = "400여기관 구매계획 발표.. 헬기 등 대형사업 상반기 조기 집행 [파이낸셜뉴스 대전=김원준 기자] 조달청은 1일 국가기관과 지방자치단체 등 400여 기관을 대상으로 실시한 외국에서 조달할 물자에 대한 사전조사 결과를 바탕으로 총 5200억 원 규모의 올해 해외물자 구매계획을 발표했다. 해외물자는 국내에서 생산 또는 공급되지 않거나 차관 자금으로 구매하는 물자를 말한다. 주요 해외물자 구매 물품은 △인구조 및 해안 경비용 대형 헬기 2대(해양경찰 560억 원·소방청 중앙119구조본부 480억 원) △소금산 케이블카(강원 원주시 85억 원) △인공환경조절(한국농수산대학 80억 원) △공항기상레이더(기상청 74억3000만원)등이 있으며, 가능한 한 올 상반기 안에 구를 추진할 예정이다. 조달청은 조달계획이 반영된 사업에 대해서는 신속하게 구매할 수 있도록 공고 기간을 기존 40일에서 25일로 단축하는 한편, 발주 규모가 큰 국가기관과 지자체 등과도 긴밀히 협력할 예정이다. 조달청 올해 해외물자 사업계획은 잠재수요 감안할 때 연말까지 목표를 달성할 것으로 전망된다. 기관별 발주 규모를 살펴보면 국가기관이 2762억 원으로 가장 많고, 이어 자체(1187억 원), 연구기관(916억 원), 기타 공공기관(338억 원) 순이다. 최호천 조달청 공공물자국장은 “해외물자 구매 예시는 계획적인 물품구매로 예산의 효율적 집행을 도모하기 위한 것”이라면서 “종합적인 입찰 정보를 사전에 제공해 해외 공급자뿐만 아니라 국내 중소업체에게도 입찰참여 기회를 부여해 안정적이고 계획성 있는 사업계획을 수립하는데 도움이 되도록 하겠다”고 했다. 한편, 조달청 해외물자 구매계획 상세 자료는 조달청 홈페이지와 나라장터(국가종합전자조달시스템)에서 열람할 수 있다. kwj5797@fnnews.com 김원준 기자"
		def createDate = "1"
		def topic = "title"
		def sentiment = "title"
		def occasion = "title"
		addDocument(INDEX_NAME, TYPE, 1, "1", title, body, createDate, topic, 
			sentiment, occasion)
	}
	void addDocument(String indexName, String typeName, int seq, 
		String articleId, String title, String body, String createDate, 
		String topic, String sentiment, String occasion) {
		def ir = client.prepareIndex(indexName, typeName, seq.toString())
				.setSource(
					TableField.FIELD1_NAME, articleId,
					TableField.FIELD2_NAME, null,
					TableField.FIELD3_NAME, null,
					TableField.FIELD4_NAME, null,
					TableField.FIELD5_NAME, title,
					TableField.FIELD6_NAME, body,
					TableField.FIELD7_NAME, null,
					TableField.FIELD8_NAME, null,
					TableField.FIELD9_NAME, null,
					TableField.FIELD10_NAME, null,
					TableField.FIELD11_NAME, null,
					TableField.FIELD12_NAME, null,
					TableField.FIELD13_NAME, createDate,
					TableField.FIELD14_NAME, null,
					TableField.FIELD15_NAME, null,
					TableField.FIELD16_NAME, null,
					TableField.FIELD17_NAME, null,
					TableField.FIELD18_NAME, null,
					TableField.FIELD19_NAME, null,
					TableField.FIELD20_NAME, null,
					TableField.FIELD21_NAME, null,
					TableField.FIELD22_NAME, null,
					TableField.FIELD23_NAME, null,
					TableField.FIELD24_NAME, null,
					TableField.FIELD25_NAME, null,
					TableField.FIELD26_NAME, null,
					TableField.FIELD27_NAME, "C",
					TableField.FIELD28_NAME, 0,
					TableField.FIELD29_NAME, body,
					TableField.FIELD30_NAME, body,
					TableField.FIELD31_NAME, body
				).execute().actionGet()
		println "version=$ir.version"
		def gr = client.prepareGet(indexName, typeName, 
			seq.toString()).execute().actionGet()
		println gr.source
	}
}