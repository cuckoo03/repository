package org.jboss.netty.example.discard;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class DiscardClient {

	/**
	 * 서버에 접속하고 서버에서는 아무런 처리를 하지 않는 예제
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		final int firstMessage = 20;

		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels
						.pipeline(new DiscardClientHandler(firstMessage) {
						});
			}
		});

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"127.0.0.1", 9001));
		System.err.println("Client connected");
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
	}
}