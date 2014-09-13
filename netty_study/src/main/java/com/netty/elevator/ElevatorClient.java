package com.netty.elevator;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.FixedLengthFrameDecoder;

public class ElevatorClient {
	public static void main(String[] args) {
		ServerBootstrap sb = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		
		sb.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline p = Channels.pipeline();
				p.addLast("handler", new ElevatorServerHandler());
				return p;
			}
		});
		
		Channel channel = sb.bind(new InetSocketAddress(8080));
		
		ClientBootstrap cb = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		cb.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline p = Channels.pipeline();

				p.addLast("frameDecoder", new FixedLengthFrameDecoder(10));
				p.addLast("customDecoder", new ElevatorFrameDecoder());
				p.addLast("handler", new ElevatorDaoHandler());

				return p;
			}
		});

		ChannelFuture future = cb.connect(new InetSocketAddress(
				"127.0.0.1", 8080)).awaitUninterruptibly();

		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			cb.releaseExternalResources();
		}

		future.getChannel().getCloseFuture().awaitUninterruptibly();

		cb.releaseExternalResources();
	}
}