package org.jboss.netty.example.portunification;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class PortUnificationClient {
	public static void main(String[] args) {
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new PortUnificationClientHandler());
			}
		});
		
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				9001));
		
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}
		
		System.err.println("client connected");
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(new String("GA").getBytes());
		channel.write(buf);
		
		channel.getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
	}
}