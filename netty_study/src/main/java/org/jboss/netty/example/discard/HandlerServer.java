package org.jboss.netty.example.discard;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class HandlerServer {

	public static void main(String[] args) throws Exception {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				ChannelPipeline p = Channels.pipeline();
				p.addLast("up1", new SimpleChannelUpstreamHandler() {
					@Override
					public void channelConnected(ChannelHandlerContext ctx,
							ChannelStateEvent e) {
						System.out.println("channelConnected1");
						ChannelBuffer buf = ChannelBuffers.buffer(10);
						buf.writeBytes(new String("hi").getBytes());
						e.getChannel().write(buf);
						
						ctx.sendUpstream(e);
					}

					@Override
					public void messageReceived(ChannelHandlerContext ctx,
							MessageEvent e) throws Exception {
						ChannelBuffer buf = (ChannelBuffer) e.getMessage();
						System.out.println("up1 messageReceived:" + buf.capacity());
						
						ctx.sendUpstream(e);
					}
				});
				p.addLast("up2", new SimpleChannelUpstreamHandler() {
					@Override
					public void channelConnected(ChannelHandlerContext ctx,
							ChannelStateEvent e) {
						System.out.println("channelConnected2");
					}
					@Override
					public void messageReceived(ChannelHandlerContext ctx,
							MessageEvent e) throws Exception {
						ChannelBuffer buf = (ChannelBuffer) e.getMessage();
						System.out.println("up2 messageReceived:" + buf.capacity());
						Thread.sleep(1000);
						e.getChannel().write(e.getMessage());
					}
				});
				return p;
			}
		});
		bootstrap.bind(new InetSocketAddress(9001));
		System.err.println("Server binded");
	}
}