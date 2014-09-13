package org.jboss.netty.codecs.json;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

public class EchoClient {
	/**
	 * 클라이언트가 보낸 메시지를 서버에서 리턴하는 예제
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline p = Channels.pipeline();
				p.addLast("framer", new DelimiterBasedFrameDecoder(4096,
						Delimiters.nulDelimiter())); // up
				p.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
				p.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
				p.addLast("handler", new EchoClientHandler(3));
				return p;
			}
		});

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"127.0.0.1", 10001));

		future.getChannel().getCloseFuture().awaitUninterruptibly();

		bootstrap.releaseExternalResources();
	}
}