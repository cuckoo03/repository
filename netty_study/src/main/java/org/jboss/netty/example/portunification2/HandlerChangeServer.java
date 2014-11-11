package org.jboss.netty.example.portunification2;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class HandlerChangeServer {

	public static void main(String[] args) {
		ServerBootstrap b = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		b.setPipelineFactory(new ChannelPipelineFactory() {

			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new ServerHandler());
			}
		});

		b.bind(new InetSocketAddress(9001));
		System.err.println("server bind");
	}
}

class ServerHandler extends SimpleChannelUpstreamHandler {
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("1".getBytes());
		e.getChannel().write(buffer);
	}
}