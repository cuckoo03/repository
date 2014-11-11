package org.jboss.netty.example.discard;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class DiscardServer {

	public static void main(String[] args) throws Exception {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(new SimpleChannelUpstreamHandler() {
					@Override
					public void messageReceived(ChannelHandlerContext ctx,
							MessageEvent e) {
						ChannelBuffer buf = (ChannelBuffer) e.getMessage();
						while (buf.readable()) {
							System.out.println("received:" + buf.readByte() + " ");
							System.out.flush();
						}
					}

					@Override
					public void exceptionCaught(ChannelHandlerContext ctx,
							ExceptionEvent e) {
						e.getCause().printStackTrace();

						Channel ch = e.getChannel();
						ch.close();
					}

					public void handleUpstream(ChannelHandlerContext ctx,
							ChannelEvent e) throws Exception {
						System.out.println("handlerUpstream server");
						super.handleUpstream(ctx, e);
					}
				});
			}
		});
		bootstrap.bind(new InetSocketAddress(9001));
		System.err.println("Server binded");

	}
}
