package com.elasticsearch.client

import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.junit.Test

import groovy.transform.TypeChecked

@TypeChecked
class DocumentAnalyzeTest {
	private Client client
	private final String INDEX_NAME1 = "media-20200201"
	private final String FIELD1_NAME = "articleId"
	private final String FIELD2_NAME = "title"
	private final String FIELD3_NAME = "body"
	private final String FIELD4_NAME = "createDate"
	private final String ELASTIC_SEARCH_IP = "broker.ip"
	private final int ELASTIC_SEARCH_PORT = 9300
	private final String CLUSTER_NAME_FIELD = "cluster.name"
	private final String CLUSTER_NAME = "tapa-es"
	private final String PROPERTIES_FIELD_NAME = "properties"
	private final String FORMAT_FIELD_NAME = "format"

	private final String LONG_FIELD_TYPE = "long"
	private final String STRING_FIELD_TYPE = "string"
	private final String DATE_FIELD_TYPE = "date"
	
	private final String FORMAT_FIELD_VALUE = "yyyyyMMddHHmmss"
	
	private final String analyzer = "my_analyzer"
	private final String tokenizer = "my_tokenizer"
	
	@Test
	void createClient() {
		def s = ImmutableSettings.settingsBuilder()
				.put(CLUSTER_NAME_FIELD, CLUSTER_NAME)
				.build();
		def tmp = new TransportClient(s);
		tmp.addTransportAddress(new InetSocketTransportAddress(
			ELASTIC_SEARCH_IP, ELASTIC_SEARCH_PORT));
		client = tmp;
		
		def text = "29일 경북 포항시 남구 이동 포항시청 인근에 겨울답지 않은 포근한 날씨가 이어지면서 매화가 피어 있다. 연합뉴스. 2월 첫날이자 토요일인 1일은 전국이 대체로 맑겠으나 아침 기온이 전날보다 3∼5도가량 떨어지겠다. 충남과 전북에는 산발적으로 빗방울이 떨어지거나 눈이 날리는 곳이 있겠다. 울릉도와 독도에는 오전까지 5㎜ 미만의 비나 1㎝ 내외의 눈이 올 것으로 예보됐다. 아침 최저기온은 영하 8∼2도 전날(영하3∼5도)보다 떨어지겠다. 낮 최고기온은 5∼12도로 올라 포근할 것으로 예상된다. 내륙 지역을 중심으로 낮과 밤의 기 차가 벌어지겠으니 건강관리에 유의해야 한다. 대기 정체로 축적된 국내 발생 미세먼지에 낮부터 국외 미세먼지가 더해져 대부분 지역에서 대기 질이 탁할 전망이다. 수도권·강원영서·충청권·호남권·대구·경북·제주권은 ‘나쁨’, 그 밖 권역은 ‘보통’ 수준으로 예상된다. 강원 산지에는 낮 동안 녹은 눈이 다시 얼어 미끄러울 수 있다. 산간 도로에선 교통안전에 주의하고 산행객도 안전사고에 유의해야 한다. 풍랑 특보가 발효 중인 동해상에는 바람이 시속 35∼60㎞로 강하게 불고 물결도 2.0∼5.0ｍ로 높게 일어 항해·조업 선박은 주의가 필요하다. 손봉석 기자 paulsohn@kyunghyang.com"
		AnalyzeRequest request = (
			new AnalyzeRequest(text.toLowerCase()))
			.index(INDEX_NAME1) 
			.analyzer(analyzer)
//			.tokenizer(tokenizer);
		List<AnalyzeResponse.AnalyzeToken> tokens = client.admin().indices()
			.analyze(request).actionGet().getTokens();
		for (AnalyzeResponse.AnalyzeToken token : tokens) {
			println token.term + " " + token.startOffset + "->" + token.endOffset + 
				" type:" + token.type + " pos:" + token.position
		}
	}
}
