package org.jboss.netty.example.echo;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class EchoServer {
	public static void main(String[] args) {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new SimpleChannelUpstreamHandler() {
					private final AtomicLong transferredBytes = new AtomicLong();

					@Override
					public void messageReceived(ChannelHandlerContext ctx,
							MessageEvent e) {
						long count = transferredBytes
								.addAndGet(((ChannelBuffer) e.getMessage())
										.readableBytes());
						System.out.println("server : messageReceived " + count);

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						e.getChannel().write(e.getMessage());
					}

					@Override
					public void exceptionCaught(ChannelHandlerContext ctx,
							ExceptionEvent e) {
						e.getCause().printStackTrace();
						e.getChannel().close();
					}
				});
			}
		});

		bootstrap.bind(new InetSocketAddress(9001));
		System.err.println("Server binded");
	}
}
