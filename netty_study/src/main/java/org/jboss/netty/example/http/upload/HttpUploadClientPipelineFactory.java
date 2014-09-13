package org.jboss.netty.example.http.upload;

import javax.net.ssl.SSLEngine;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.example.securechat.SecureChatSslContextFactory;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

public class HttpUploadClientPipelineFactory implements ChannelPipelineFactory {
	private final boolean ssl;

	public HttpUploadClientPipelineFactory(boolean ssl) {
		this.ssl = ssl;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

		if (ssl) {
			SSLEngine engine = SecureChatSslContextFactory.getClientContext()
					.createSSLEngine();
			engine.setUseClientMode(true);

			SslHandler handler = new SslHandler(engine);
			handler.setIssueHandshake(true);
			pipeline.addLast("ssl", handler);
		}

		pipeline.addLast("codec", new HttpClientCodec());
		pipeline.addLast("inflater", new HttpContentDecompressor());
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		pipeline.addLast("handler", new HttpUploadClientHandler());
		return pipeline;
	}
}