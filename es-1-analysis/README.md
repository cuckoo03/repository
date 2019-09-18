elasticsearch 1.x 분석기 플러그인

빌드
>mvn clean install
빌드후 target폴더에 있는 .jar파일의 확장자를 zip으로 변경

설치방법
elasticsearch 설치 서버에 임의의 폴더에 복사하고 플러그인 명령어로 인스톨 실행
>es설치폴더/bin/plugin --url  file:///home/service/es-1-analysis-1.0.0.zip --install 플러그인명
플러그인명=AnalysisMyPlugin.java에 설정한 이름

인스톨 확인
>es설치폴더/bin/plugin --list

플러그인 제거
>es설치폴더/bin/plugin --remove 플러그인명 

1.0.0 kr_analyzer 사용시 에러(키워드:1방 탄 소년 단 R T 1), standard analyzer는 에러 발생안함.
org.elasticsearch.common.util.concurrent.UncategorizedExecutionException: Failed execution
	at org.elasticsearch.action.support.AdapterActionFuture.rethrowExecutionException(AdapterActionFuture.java:90)
	at org.elasticsearch.action.support.AdapterActionFuture.actionGet(AdapterActionFuture.java:50)
	at org.elasticsearch.action.ActionFuture$actionGet.call(Unknown Source)
	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:45)
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:108)
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:112)
	at com.elasticsearch.client.DocumentAnalyzeTest.createClient(DocumentAnalyzeTest.groovy:51)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	at java.lang.reflect.Method.invoke(Unknown Source)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:45)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:15)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:42)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:20)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:263)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:68)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:47)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:231)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:60)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:229)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:50)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:222)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:300)
	at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:86)
	at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:459)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:678)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:382)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:192)
Caused by: java.lang.IllegalStateException: TokenStream contract violation: reset()/close() call missing, reset() called multiple times, or subclass does not call super.reset(). Please see Javadocs of TokenStream class for more information about the correct consuming workflow.
	at org.apache.lucene.analysis.Tokenizer$1.read(Tokenizer.java:110)
	at org.apache.lucene.analysis.kr.KoreanTokenizerImpl.zzRefill(KoreanTokenizerImpl.java:566)
	at org.apache.lucene.analysis.kr.KoreanTokenizerImpl.getNextToken(KoreanTokenizerImpl.java:763)
	at org.apache.lucene.analysis.kr.KoreanTokenizer.incrementToken(KoreanTokenizer.java:134)
	at org.apache.lucene.analysis.kr.KoreanFilter.incrementToken(KoreanFilter.java:134)
	at org.apache.lucene.analysis.core.LowerCaseFilter.incrementToken(LowerCaseFilter.java:54)
	at org.apache.lucene.analysis.util.FilteringTokenFilter.incrementToken(FilteringTokenFilter.java:82)
	at org.elasticsearch.action.admin.indices.analyze.TransportAnalyzeAction.shardOperation(TransportAnalyzeAction.java:211)
	at org.elasticsearch.action.admin.indices.analyze.TransportAnalyzeAction.shardOperation(TransportAnalyzeAction.java:55)
	at org.elasticsearch.action.support.single.custom.TransportSingleCustomOperationAction$AsyncSingleAction$2.run(TransportSingleCustomOperationAction.java:174)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)

