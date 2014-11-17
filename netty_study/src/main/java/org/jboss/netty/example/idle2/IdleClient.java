package org.jboss.netty.example.idle2;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class IdleClient {

	public static void main(String[] args) {
		NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		final ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {

				ChannelPipeline p = Channels.pipeline();
				p.addLast("up", new UpHandler());
				p.addLast("down", new DownHandler());
				return p;
			}
		});

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"192.168.1.100", 9001));
		future.awaitUninterruptibly();
		if (!future.isSuccess()) {
			future.getCause();
			bootstrap.releaseExternalResources();
			return;
		}
		System.out.println("client connect");
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
	}
}

class UpHandler extends SimpleChannelUpstreamHandler {
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		System.out.println("channelConnected");
		e.getChannel().write(
				ChannelBuffers.copiedBuffer("client send".getBytes()));
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		System.out.println("client messageREceived");
		e.getChannel().write(e.getMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		System.out.println(e.getCause().getMessage());
		e.getChannel().close();
	}
}

class DownHandler extends SimpleChannelDownstreamHandler {
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws InterruptedException {
		System.out.println("client writeRequested");
		ctx.sendDownstream(e);
	}
}
