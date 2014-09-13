package org.jboss.netty.example.local;

import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.local.DefaultLocalClientChannelFactory;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.jboss.netty.channel.local.LocalAddress;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

public class LocalExampleMultithreaded {
	public static void main(String[] args) {
		LocalAddress socketAddress = new LocalAddress("1");

		OrderedMemoryAwareThreadPoolExecutor eventExecutor = new OrderedMemoryAwareThreadPoolExecutor(
				5, 1000000, 10000000, 100, TimeUnit.MILLISECONDS);

		ServerBootstrap sb = new ServerBootstrap(
				new DefaultLocalServerChannelFactory());

		sb.setPipelineFactory(new LocalServerPipelineFactory(eventExecutor));
		sb.bind(socketAddress);

		ClientBootstrap cb = new ClientBootstrap(
				new DefaultLocalClientChannelFactory());

		cb.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				/*
				return Channels.pipeline(new StringEncoder(),
						new StringDecoder() , new LoggingHandler(
								InternalLogLevel.INFO));
								*/
				ChannelPipeline p = Channels.pipeline();
				p.addLast("encoder", new StringEncoder());
				p.addLast("decoder", new StringDecoder());
				return p;
			}
		});

		String[] commands = { "first", "second", "thrid", "quit" };
		for (int i = 0; i < 4; i++) {
			System.err.println("Start:" + i);
			ChannelFuture channelFuture = cb.connect(socketAddress);
			channelFuture.awaitUninterruptibly();
			if (!channelFuture.isSuccess()) {
				System.err.println("cannot connect");
				channelFuture.getCause().printStackTrace();
				break;
			}
			ChannelFuture lastWriteFuture = null;
			for (String line : commands) {
				lastWriteFuture = channelFuture.getChannel().write(line);
			}

			if (null != lastWriteFuture) {
				lastWriteFuture.awaitUninterruptibly();
			}
			channelFuture.getChannel().close();

			channelFuture.getChannel().getCloseFuture().awaitUninterruptibly();
			System.err.println("end:" + i);
		}

		cb.releaseExternalResources();
		sb.releaseExternalResources();
		eventExecutor.shutdown();
	}
}