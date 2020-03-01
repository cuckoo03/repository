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
		
		def text = "[서울신문] 오현중 - 사진=인스타그램 신종 코로나 바이러스 감염증이 유행하는 가운데, 배우 오현중이 택시에 마스크를 비치한 사실이 알려졌다. 지난 30일 한 온라인 커뮤니티에 올라 온 글에 따르면, 오현중은 택시 기사로 일하는 아버지에게 방역 마스크를 드린 뒤 택시 승객을 위해서도 뒷자리에 여분의 마스크를 다수 비치했다. 글에 따르면, 글 작성자의 여자친구는 새로 구매한 마스크를 잃어버린 상태에서 택시를 탔다. 택시기사는 좌석 왼쪽 공간에 다수의 승객용 마스크가 비치돼 있다고 안내했다. 택시에 비치된 마스크에는 '아버지, 마스크 꼭 하고 다니세요. 혹여나 승객들이 무섭다고 오해할 수도 있으니, 뒷좌석에 승객용으로도 놔두시고 사용하세요. 부족하면 더 살게. 사랑해♡'라고 쓰인 글귀가 적혀 있었따. 글쓴이의 여자친구가 해당 메모를 작성한 사람에 대해 묻자, 택시 기사는 \"아들이다. 유명하지는 않은데 배우\"라고 답했다. 글쓴이는 검색을 통해 오현중의 인스타그램을 찾은 뒤 메시지로 감사 인사를 보내며 정말 배우가 맞는지 물었다. 이에 오현중은 \"안녕하세요. 저 맞습니다. 인증 메시지 덕분에 오늘 하루가 되게 뜻깊네요. 감사합니다. 마스크 항시 잘 착용하시고 건강하세요\"라고 답했다. 임효진 기자 3a5a7a6a@seoul.co.kr"
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
