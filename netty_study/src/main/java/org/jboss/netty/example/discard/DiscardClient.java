package org.jboss.netty.example.discard;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class DiscardClient {

	/**
	 * 서버에 접속하고 서버에서는 아무런 처리를 하지 않는 예제
	 * @param args
	 */
	public static void main(String[] args) {

		final int firstMessage = 2;

		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		
		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels
						.pipeline(new DiscardClientHandler(firstMessage));
			}
		});

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"127.0.0.1", 8080));
		
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		
		bootstrap.releaseExternalResources();
	}
}