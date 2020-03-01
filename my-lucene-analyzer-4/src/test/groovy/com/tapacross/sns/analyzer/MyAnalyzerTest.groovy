package com.tapacross.sns.analyzer

import groovy.transform.TypeChecked

import java.io.IOException
import java.io.StringReader

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.core.SimpleAnalyzer
import org.apache.lucene.analysis.core.WhitespaceAnalyzer
import org.apache.lucene.analysis.ngram.NGramTokenizer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute
import org.apache.lucene.analysis.tokenattributes.TypeAttribute
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.Term
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.WildcardQuery
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.util.Version
import org.junit.Ignore
import org.junit.Test

@TypeChecked
class MyAnalyzerTest {
	/**
	 * 형태소분석 실패시: 방탄소년단rt1 0->8 ON
	 * @throws IOException
	 */
	@Test
	public void testAnalyze() throws IOException {
//		String text = "1방 탄 소 년 단 R T 1".toLowerCase()
//		String text = "RT우리짐건 입금을 시작합니다! !?"
//		final def text = "ㅋㅋㅋ불치병입니다ㅋㅋㅋㅋㅋㅋㅋㅋㅋ"
//		final def text = "RT [아마존 재팬] [예약접수중] 「울트라맨 타이가」로부터, 변신 아이템 「DX 타이가 스파크」와, 「DX 울트라맨 타이가 완전 변신 세트」가 등장! 구입특전 「울트라맨 타로릿」이 붙은 제품은 이쪽으로 ⇒ https://amzn.to/2QwLxve  (※ 수량이 다하는 대로 예약이 종료됩니다.)"
//		def text = "퍼스트A가 아이템전, 에이스 결정전에서 승리, 엑스퀘어를 꺾고 첫승을 기록했다. 1일 서울 넥슨아레나에서 진행된 '2020 카트라이더 리그 시즌1' 8강 15경기에서 퍼스트A가 에이스 결정전까지 가는 접전 끝에 승리를 가져왔다. 퍼스트A는 스피드전에서 패배했으나 아이템전과 에이스 결정전에서 승점을 가져와 첫승을 기록했다. 스피드전 1트랙 어비스 바다 소용돌이에선 장건이 치고 나가며 2랩 선두를 차지했다. 안혁진과 이현진은 장건을 쫓았지만 뒷심이 발휘되지 않아 퍼스트A에게 원투를 내줬다. 대저택 은밀한 지하실에선 퍼스트A의 양민규와 엑스퀘어 오성현이 스타트를 치고 나갔다. 장건은 안혁진과 이현진을 쫓아갔지만 2랩 마지막 순간에 엑스퀘어가 역전으로 승점을 가져갔다. 1대1 상황, 양민규는 광산 아슬아슬 궤도 전차에서 치고 나가며 에이스 역할을 맡았지만 추락 사고와 벽에 부딪히는 사고가 연달아 발생했다.  엑스퀘어는 안혁진의 분전에도 8위로 고정된 오성현과 2위를 수성하지 못한 이현진의 사고로 4세트를 내줬다. 5트랙은 노르테유 익스프레스에서 진행됐다. 두 팀은 초반 몸싸움에서 밀리지 않기 위해 천천히 서행하는 전략을 펼쳤다. 엑스퀘어는 노창현의 초반 추락 사고로 뒤쳐졌으나 퍼스트A에서도 실수가 나왔다. 엑스퀘어는 1, 3, 4위로 골인에 성공해 3대2로 스피드전 승리를 가져왔다. 아이템전 어비스 지상으로 가는 길에서 역전을 당한 엑스퀘어는 2트랙 아이스 신나는 하프파이프에서 퍼스트A의 황금 자석을 무위로 돌리며 승점을 가져왔다. 그러나 퍼스트A는 차이나 방등축제에서 장건을 여유롭게 선두로 골인시켜 다시 포인트를 앞섰다. 광산 뽀글뽀글 용암굴에선 자석과 얼음 폭탄의 치열한 눈치 싸움 끝에 엑스퀘어가 득점했다. 퍼스트A는 마지막 5세트를 잡으며 에이스 결정전까지 가는데 성공했다. 에이스 결정전은 노르테유 익스프레스에서 펼쳐졌다. 엑스퀘어에선 안혁진이, 퍼스트A는 임재원이 에이스로 등장했다. 안혁진은 초반에 가속을 올렸고, 임재원 역시 1랩 중반에 치고 나갔다. 노르테유 익스프레스의 난이도에 두 선수 모두 사고에 휘말렸다. 하지만 안혁진이 추락하며 임재현이 승리를 쟁취했다. 서초 | 모경민 기자 raon@fomos.co.kr 포모스와 함께 즐기는 e스포츠, 게임 그 이상을 향해!"
//		def text = "처음 뵙겠습니다 당신의 집짓기 생활을 이롭게 하는 신자재들이 건축 시장에 출사표를 던진다. 혁신적인 기술과 진보된 상상력으로 무장한 제품 중 다섯 가지를 엄선했다. 1. 루버로 완성하는 아트월 일반 벽지는 심심하고 석재 아트월은 부담스럽다면 목재 루버 시스템으로 집에 포인트를 주는 건 어떨까. 케이디우드테크에서 출시한 '펄스 루버 시스템'은 마운틴 루버(방염 제품)와 트래디션 루버의 조합으로 만들어진 제품으로, 인체의 맥박을 부호화하여 이름을 붙였다. 100% 오동나무 원목으로 만들어졌으며, 수평과 피라미드의 반복적인 율동감이 공간에 활력을 불어넣는다. 일반 소나무 루버와 비교해 무게는 40% 수준이라 벽뿐만 아니라 천장에 설치해도 부담이 없으며, 제혀쪽매 방식은 빠르고 간편한 시공을 보장한다.  ▶ ㈜케이디우드테크 www.kdwoodtech.com 2. 줄눈이 필요 없는 벽돌 처마 없는 박공지붕, 요철 없이 단순한 매스. 2020년에도 심플한 디자인에 대한 인기는 여전할 전망이다. 여기에 줄눈 없는 '노 조인트(No Joint) 벽돌'이 힘을 싣는다. 제품은 줄눈의 두께를 12mm에서 4mm로 극도로 얇게 함으로써 외관상 깔끔하면서도 벽돌 조적 작업을 더 빠르고 쉽게 한다. 2019년 굿디자인상(GD) 수상과 더불어 특허청에 디자인 등록된 이 점토 미장 벽돌은 시공자에 따라 품질이 다를 수 있는 조적의 품질을 균질화하고, 줄눈이 벽면 외관의 색감에 거의 미치지 않는다는 장점도 갖췄다.  ▶ ㈜선일로에스 www.sunilind.co.kr 3. 건축가와 협업한 스틸 모듈하우스 이동식 주택이나 농막 등 6평 미만의 소형 주택에 대한 관심이 갈수록 커지고 있다. 재료 가공, 시공성, 이동 등을 이유로 경량목구조 비중이 높은 시장에 스틸하우스가 도전장을 내밀었다. 금속 가공 및 가구 전문기업인 인페쏘가 건축사사무소 스튜디오 포마와 협업해 스틸 모듈하우스를 만든 것이다. 기존 모듈하우스와 달리 용접 대신 파이프레이저로 가공하고 조립하여 구조를 완성했기에 경제적이면서 생산성이 좋고 시공 품질 역시 뛰어나다. 외부마감재는 포스코의 신소재인 '포스맥 컬러강판'을 사용, 녹이 슬지 않고 스크래치에 강하며 도장 공정을 거치지 않아도 된다. ▶ 인페쏘 http://infeso.com 4. 우주를 담은 타일 석재 타일은 거칠면서도 고급스러운 매력을 뿜어내며 전천후 내·외장재로 사용되고 있다. 넥스트타일에서 출시한 '갤럭시 락' 시리즈는 은하수를 모티브로 한 포세린 타일로 6가지 패턴으로 구성돼 실제 돌을 사용하는 듯한 느낌을 선사한다. 거친 입자와 미세 입자를 조합해 표면 질감이 더욱 사실적이고 보다 섬세하게 표현되어 있으며, 우주의 별처럼 반짝이는 미려함과 중후한 매력을 더한다. 실내 어느 공간에 사용해도 조화로운 인테리어를 구현하며, 물 흡수율 0.1% 미만, 600x1,200(mm)의 사이즈라 외벽 마감재로 사용할 수도 있다.  ▶ 넥스트타일 www.nexttile.kr 5. 다양한 패턴의 PVC 바닥재 실용성 있는 바닥재를 찾는 건축주라면 PVC 바닥재를 눈여겨보자. 신규 디자인 패턴을 추가하며 리뉴얼한 'KCC숲 블루·옥'이 최신 인테리어 트렌드를 반영한 새 제품을 출시했다. KCC숲 블루와 옥은 환경표지인증, HB마크 최우수 등급 등 국내 주요 친환경 인증마크를 획득했으며, 논슬립(Non-slip) 바닥재라 아이 또는 노인이 함께 생활하는 집에 추천하는 제품이다. 이번 리뉴얼을 통해 감성을 더하는 우드, 거친 느낌의 콘크리트, 천연 대리석 등 다양한 패턴이 추가되었으며, 무늬와 엠보싱을 일치시켜 질감을 사실적으로 구현하는 '동조 엠보스' 기술도 적용했다. ▶ KCC www.kccworld.co.kr 구성 _ 조성일 ⓒ 월간 전원속의 내집 2020년 2월호 / Vol.252 www.uujj.co.kr"
		def text = "[서울신문] 오현중 - 사진=인스타그램 신종 코로나 바이러스 감염증이 유행하는 가운데, 배우 오현중이 택시에 마스크를 비치한 사실이 알려졌다. 지난 30일 한 온라인 커뮤니티에 올라 온 글에 따르면, 오현중은 택시 기사로 일하는 아버지에게 방역 마스크를 드린 뒤 택시 승객을 위해서도 뒷자리에 여분의 마스크를 다수 비치했다. 글에 따르면, 글 작성자의 여자친구는 새로 구매한 마스크를 잃어버린 상태에서 택시를 탔다. 택시기사는 좌석 왼쪽 공간에 다수의 승객용 마스크가 비치돼 있다고 안내했다. 택시에 비치된 마스크에는 '아버지, 마스크 꼭 하고 다니세요. 혹여나 승객들이 무섭다고 오해할 수도 있으니, 뒷좌석에 승객용으로도 놔두시고 사용하세요. 부족하면 더 살게. 사랑해♡'라고 쓰인 글귀가 적혀 있었따. 글쓴이의 여자친구가 해당 메모를 작성한 사람에 대해 묻자, 택시 기사는 \"아들이다. 유명하지는 않은데 배우\"라고 답했다. 글쓴이는 검색을 통해 오현중의 인스타그램을 찾은 뒤 메시지로 감사 인사를 보내며 정말 배우가 맞는지 물었다. 이에 오현중은 \"안녕하세요. 저 맞습니다. 인증 메시지 덕분에 오늘 하루가 되게 뜻깊네요. 감사합니다. 마스크 항시 잘 착용하시고 건강하세요\"라고 답했다. 임효진 기자 3a5a7a6a@seoul.co.kr"

		def analyzer = new MyAnalyzer()
		TokenStream stream = analyzer.tokenStream("f", new StringReader(text))
		// http://www.hankcs.com/program/java/lucene-4-6-1-java-lang-illegalstateexception-tokenstream-contract-violation.html
		stream.reset()
		printTerms(stream)
		stream.close()
		System.out.println("------------");

//		text = "한글 이상b"
//		stream = analyzer.tokenStream("f", new StringReader(text))
//		stream.reset()
//		printTerms(stream)
//		stream.close()

		// parse 3
		/*
		text = "한글 이상c"
		stream = analyzer.tokenStream("f", new StringReader(text))
		stream.reset()
		printTerms(stream)
		stream.close()
		*/
		
//		repeat(analyzer, text)

		analyzer.close()
	}

	@Test
	@Ignore
	public void testIndex() throws IOException {
		String text = "버튼을 이용하여 분석식에"
		
		def analyzer = new MyAnalyzer()
		
		Directory dir = new RAMDirectory()
//		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41)
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, analyzer)
		IndexWriter indexWriter = new IndexWriter(dir, config)

		Document doc = new Document()
		def stringField = new TextField("name", "", Field.Store.YES)
		stringField.setStringValue(text)
		doc.add(stringField)
		indexWriter.addDocument(doc, analyzer)

		indexWriter.commit()

		IndexReader indexReader = DirectoryReader.open(dir)
		IndexSearcher indexSearcher = new IndexSearcher(indexReader)
		def query = new TermQuery(new Term("name", "버튼"))
		def topDocs = indexSearcher.search(query, 1)
		for (def scoreDoc : topDocs.scoreDocs) {
			doc = indexSearcher.doc(scoreDoc.doc)
			println "find=" + doc.getField("name").stringValue()
		}
	}
	
	private void printTerms(TokenStream stream) throws IOException {
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);
		OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
		TypeAttribute type = stream.addAttribute(TypeAttribute.class);
		PayloadAttribute payload = stream.addAttribute(PayloadAttribute.class);
		int position = 0;

		while (stream.incrementToken()) {
//			int increment = posIncr.getPositionIncrement();
//			if (increment > 0) {
//				position = position + increment;
//				System.out.print(position + ": ");
//			}

			System.out
					.print(posIncr.getPositionIncrement() + " " + 
						term.toString() + " " + offset.startOffset() + 
						"->" + offset.endOffset() + " type:" + type.type() +
						" payload:" + payload.payload.utf8ToString()
						);
			System.out.println();
			System.out.println();
		}
	}
}
