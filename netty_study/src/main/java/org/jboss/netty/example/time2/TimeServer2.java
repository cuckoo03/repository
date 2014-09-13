package org.jboss.netty.example.time2;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.example.time.TimeEncoder;

public class TimeServer2 {
	static final ChannelGroup allChannels = new DefaultChannelGroup(
			"time-server");

	public static void main(String[] args) {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new TimeEncoder(),
						new TimeServerHandler2());
			}
		});

		Channel channel = bootstrap.bind(new InetSocketAddress(10001));
		allChannels.add(channel);
		try {
			// 종료 신호를 대기하는 가상의 메서드
			// waitForShutdownCommand();
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("TimeServer is closing");
		
		java.util.Iterator<Channel> iter = allChannels.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next().toString());
		}
		
		ChannelGroupFuture future = allChannels.close();
		future.awaitUninterruptibly();
		factory.releaseExternalResources();
	}

}
