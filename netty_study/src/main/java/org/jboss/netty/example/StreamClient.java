package org.jboss.netty.example;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.SocketChannel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class StreamClient {
	public static void main(String[] args) {
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			ChannelPipeline p = Channels.pipeline();

			public ChannelPipeline getPipeline() throws Exception {
				p.addLast("1", new ClientUpStreamHandlerA());
				return p;
			}
		});

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(8080));
		future.awaitUninterruptibly();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}
		System.out.println("main:connected");
		
		future.getChannel().close();
		
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		
		System.out.println("main:closed");
		
		bootstrap.releaseExternalResources();
	}
}
