package com.netty.codecs.replayingdecoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class ReplayingDecoderClient {

	public static void main(String[] args) {
		ReplayingDecoderClient client = new ReplayingDecoderClient();
		client.start();
	}

	private void start() {
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap
				.setPipelineFactory(new ReplayingDecoderClientPipelineFactory());

		ChannelFuture future = bootstrap.connect(
				new InetSocketAddress("127.0.0.1", 10001))
				.awaitUninterruptibly();

		if (!future.isSuccess()) {
			future.getChannel().close();
			bootstrap.releaseExternalResources();
			return;
		}

		future.getChannel().getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
	}
}

class ReplayingDecoderClientPipelineFactory implements ChannelPipelineFactory {
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline p = Channels.pipeline();
		p.addLast("encoder", new MyReplayingEncoder());
		p.addLast("decoder", new MyReplayingDecoder());
		p.addLast("handler", new ReplayingClientHandler());
		return p;
	}
}

class ReplayingClientHandler extends SimpleChannelUpstreamHandler {
	private final Log log = LogFactory.getLog(getClass());

	/**
	 * unique한 클라이언트 id를 패킷에 담아 전송한다.
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		e.getChannel().write(
				new Envelope(Version.VERSION1, Type.TYPE1, new String("env1")
						.getBytes()));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}

	/**
	 * receive한 메시지를 화면에 출력한다
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (e.getMessage() instanceof Envelope) {
			System.out.println(((Envelope) e.getMessage()).toString());
		}
	}
}