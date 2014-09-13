package com.netty.codecs.replayingdecoder;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

public class ReplayingDecoderServer {

	public static void main(String[] args) {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		OrderedMemoryAwareThreadPoolExecutor eventExecutor = new OrderedMemoryAwareThreadPoolExecutor(
				4, 1000000, 10000000, 30, TimeUnit.MILLISECONDS);

		bootstrap.setPipelineFactory(new LogCrawlerServerPipelineFactory(
				eventExecutor));

		Map map = bootstrap.getOptions();

//		bootstrap.setOption("reuseAddress", true);
//		bootstrap.setOption("child.tcpNoDelay", true);
//		bootstrap.setOption("child.keepAlive", true);

		bootstrap.bind(new InetSocketAddress(10001));

		System.out.println("server started..");

		ChannelPipelineFactory p = bootstrap.getPipelineFactory();
		ChannelHandler handler;
		try {
			handler = p.getPipeline().getLast();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class LogCrawlerServerPipelineFactory implements ChannelPipelineFactory {
	private SessionFactory factory = null;
	private final AtomicLong receiveCount = new AtomicLong(0);
	private ExecutionHandler executionHandler;

	public LogCrawlerServerPipelineFactory(
			OrderedMemoryAwareThreadPoolExecutor eventExecutor) {
		factory = new Configuration().configure().buildSessionFactory();
		this.executionHandler = new ExecutionHandler(eventExecutor);
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline p = Channels.pipeline();
		p.addLast("encoder", new MyReplayingEncoder());
		p.addLast("decoder", new MyReplayingDecoder());
		p.addLast("executor", executionHandler);
		p.addLast("handler", new LogCrawlerServerHandler(factory, receiveCount));
		return p;
	}
}

class LogCrawlerServerHandler extends SimpleChannelUpstreamHandler {
	private final Log log = LogFactory.getLog(getClass());
	private final SessionFactory factory;
	private final AtomicLong receiveCount;

	public LogCrawlerServerHandler(SessionFactory factory,
			AtomicLong receiveCount) {
		this.factory = factory;
		this.receiveCount = receiveCount;
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		log.info("channelConnected:");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}

	/**
	 * 클라이언트 Id를 전달 받고 db에 저장.
	 * <p>
	 * 클라이언트에게 응답메시지를 패킷에 담아 보냄 응답
	 * <p>
	 * write후에 채널 close
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (e.getMessage() instanceof Envelope) {
			Envelope env = (Envelope) e.getMessage();
			env.setVersion(Version.VERSION2);
			log.info("env:" + env.toString());

			ChannelFuture future = e.getChannel().write(env);

			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future)
						throws Exception {
					future.getChannel().close();
				}
			});
		} else {
			log.info("cast error");
		}
	}
}