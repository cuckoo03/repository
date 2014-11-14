package org.jboss.netty.example.time;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class TimeServer {
	static final ChannelGroup allChannels = new DefaultChannelGroup(
			"time-server");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new TimeEncoder(),
						new TimeServerHandler());
			}
		});

		Channel channel = bootstrap.bind(new InetSocketAddress(9001));
		allChannels.add(channel);
		// 종료 신호를 대기하는 가상의 메서드
		// waitForShutdownCommand();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/*
		 * ChannelGroupFuture future = allChannels.close();
		 * future.awaitUninterruptibly();
		 * System.out.println("server channl await");
		 * 
		 * factory.releaseExternalResources();
		 * System.out.println("server resource close");
		 */
	}

}
