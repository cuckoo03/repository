package org.jboss.netty.example.http.snoop;

import javax.net.ssl.SSLEngine;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.example.securechat.SecureChatSslContextFactory;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.ssl.SslHandler;

public class HttpClientPipelineFactory implements ChannelPipelineFactory {
	private final boolean ssl;

	public HttpClientPipelineFactory(boolean ssl) {
		this.ssl = ssl;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

		if (ssl) {
			SSLEngine engine = SecureChatSslContextFactory.getClientContext()
					.createSSLEngine();

			engine.setUseClientMode(true);

			pipeline.addLast("ssl", new SslHandler(engine));
		}

		// 다운스트림에 인코더를 업스트림에 디코더를 사용한다. 데이터를 전송할 때는 전송하기
		// 위한 적절한 포맷으로 변환해주고 데이터를 수신할 때는 다시 그 데이터를 디코딩한다.
		// 서버쪽에서도 동일한 구조를 가지고 있어야 정상적으로 데이터를 주고 받을 수 있다.
		pipeline.addLast("codec",
				new org.jboss.netty.handler.codec.http.HttpClientCodec());

		// 수신된 데이터가 압축되어 있다면 이를 풀어주는 역할을 한다.(Http 헤더를 보고 판단한다.)
		pipeline.addLast("inflater", new HttpContentDecompressor());

		pipeline.addLast("handler", new HttpClientResponseHandler());

		return pipeline;
	}
}
