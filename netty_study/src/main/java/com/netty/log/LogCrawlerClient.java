package com.netty.log;

import java.net.InetSocketAddress;
import java.util.Random;
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
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class LogCrawlerClient {

	public static void main(String[] args) {
		
	}

	public LogCrawlerClient() {
		start();
	}
	private void start() {
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new LogCralwerClientPipelineFactory());

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

class LogCralwerClientPipelineFactory implements ChannelPipelineFactory {
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline p = Channels.pipeline();
		p.addLast("delimiter",
				new DelimiterBasedFrameDecoder(8192, Delimiters.nulDelimiter()));
		p.addLast("encoder", new StringEncoder());
		p.addLast("decoder", new StringDecoder());
		p.addLast("handler", new LogCrawlerClientHandler());
		return p;
	}
}

class LogCrawlerClientHandler extends SimpleChannelUpstreamHandler {
	private final Log log = LogFactory.getLog(getClass());
	// private final static String END = "\r\n";
	private final static String END = new String(new char[] { 0 });

	/**
	 * unique한 클라이언트 id를 패킷에 담아 전송한다.
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		Random r = new Random();
		String msg = r.nextLong() + "|" + END;
		e.getChannel().write(msg);
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
		String msg = (String) e.getMessage();
		log.info("client:" + msg);
	}
}