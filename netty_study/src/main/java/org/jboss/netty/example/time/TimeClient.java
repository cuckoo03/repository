package org.jboss.netty.example.time;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class TimeClient {
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = Integer.parseInt("9001");

		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new TimeDecoder(),
						new TimeClientHandler());
			}
		});

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));
		future.awaitUninterruptibly();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
		}

		future.getChannel().getCloseFuture().awaitUninterruptibly();
		factory.releaseExternalResources();
	}
}
