package org.jboss.netty.example.http.snoop;

import javax.net.ssl.SSLEngine;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.example.securechat.SecureChatSslContextFactory;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.ssl.SslHandler;

public class HttpServerPipelineFactory implements ChannelPipelineFactory {
	private final Boolean ssl;

	public HttpServerPipelineFactory(Boolean ssl) {
		this.ssl = ssl;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

		if (ssl) {
			SSLEngine engine = SecureChatSslContextFactory.getServerContext()
					.createSSLEngine();
			engine.setUseClientMode(false);
			
			pipeline.addLast("ssl", new SslHandler(engine));
		}
//		pipeline.addLast("codec",
//				new org.jboss.netty.handler.codec.http.HttpServerCodec());
		// HttpRequestDecoder, HttpResponseEnder대신 
		// HttpServerCodec을 사용해도 된다.
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("deflater", new HttpContentCompressor());
		pipeline.addLast("handler", new HttpServerRequestHandler());

		return pipeline;
	}
}