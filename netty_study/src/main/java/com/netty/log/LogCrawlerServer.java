package com.netty.log;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
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
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import com.netty.log.domain.LogTable;

public class LogCrawlerServer {

	public static void main(String[] args) {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		OrderedMemoryAwareThreadPoolExecutor eventExecutor = new OrderedMemoryAwareThreadPoolExecutor(
				4, 1000000, 10000000, 30, TimeUnit.MILLISECONDS);

		bootstrap.setPipelineFactory(new LogCrawlerServerPipelineFactory(
				eventExecutor));

		System.out.println("keepalive:" + bootstrap.getOption("keepAlive"));
		System.out.println("tcpNoDelay:" + bootstrap.getOption("tcpNoDelay"));
		
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("child.keepAlive", true);
		Map map = bootstrap.getOptions();
		
		bootstrap.bind(new InetSocketAddress(10001));
		
		ChannelPipelineFactory p = bootstrap.getPipelineFactory();
		ChannelHandler handler;
		try {
			handler = p.getPipeline().getLast();
//			new ThroughputMonitor(handler).start();
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
		p.addLast("delimiter",
				new DelimiterBasedFrameDecoder(8192, Delimiters.nulDelimiter()));
		p.addLast("encoder", new StringEncoder());
		p.addLast("decoder", new StringDecoder());
		p.addLast("executor", executionHandler);
		p.addLast("handler", new LogCrawlerServerHandler(factory, receiveCount));
		return p;
	}
}

class LogCrawlerServerHandler extends SimpleChannelUpstreamHandler {
	private final Log log = LogFactory.getLog(getClass());
	private final SessionFactory factory;
	private final AtomicLong receiveCount;
	// private final static String END = "\r\n";
	private final static String END = new String(new char[] { 0 });

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
		log.error("unexpected exception:" + e.getCause());
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
		String msg = (String) e.getMessage();
		String[] arr = msg.split("[|]");
		String clientId = arr[0];
		String returnMsg = "";

		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		boolean close = true;
		// business logic
		// synchronized (receiveCount) {
		System.out.println("sync start-------------------");
		try {
			Long count = receiveCount.getAndIncrement();
			Calendar cal = Calendar.getInstance();
			Timestamp timestamp = new Timestamp(cal.getTimeInMillis());

			LogTable logt = null;
			logt = new LogTable();
			logt.setClientId(Long.valueOf(clientId));
			logt.setReceiveData(String.valueOf(count));
			logt.setReceiveTime(timestamp);

			session.save(logt);

			returnMsg += "[" + clientId;
			returnMsg += "|" + count.toString();
			returnMsg += "|" + timestamp.toString();
		} catch (Exception ex) {
			returnMsg += "|" + ex.getMessage();
			close = false;
		} finally {
			tx.commit();
			session.close();
		}
		System.out.println("sync end-------------------.");
		// }

		returnMsg += "]" + END;

		ChannelFuture future = e.getChannel().write(returnMsg);
		/*
		if (close) {
			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future)
						throws Exception {
					future.getChannel().close();
				}
			});
		}
		*/
	}

	public Long getReceiveData() {
		return receiveCount.get();
	}
}