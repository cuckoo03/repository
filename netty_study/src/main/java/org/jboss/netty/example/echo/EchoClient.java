package org.jboss.netty.example.echo;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

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
				return Channels.pipeline(new EchoClientHandler(3));
			}
		});

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"127.0.0.1", 9001));
		System.err.println("Client connected");

		future.getChannel().getCloseFuture().awaitUninterruptibly();

		bootstrap.releaseExternalResources();
	}
}