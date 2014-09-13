package org.jboss.netty.example.securechat;

import javax.net.ssl.SSLEngine;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.ssl.SslHandler;

public class SecureChatServerPipelineFactory implements ChannelPipelineFactory {
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline p = Channels.pipeline();

		SSLEngine engine = SecureChatSslContextFactory.getServerContext()
				.createSSLEngine();
		engine.setUseClientMode(false);

		p.addLast("ssl", new SslHandler(engine));

		p.addLast(
				"framer",
				new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		p.addLast("decoder", new StringDecoder());
		p.addLast("encoder", new StringEncoder());

		p.addLast("handler", new SecureChatServerHandler());
		return p;
	}

}
