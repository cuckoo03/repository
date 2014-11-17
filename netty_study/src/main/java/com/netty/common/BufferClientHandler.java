package com.netty.common;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutException;
import org.jboss.netty.handler.timeout.WriteTimeoutException;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;

public class BufferClientHandler extends SimpleChannelUpstreamHandler {
	private final Log log = LogFactory.getLog(getClass());
	private final Timer timer;
	private final ClientBootstrap bootstrap;
	private final List<DataChangeEventListener> list;

	public BufferClientHandler(Timer timer, ClientBootstrap bootstrap,
			List<DataChangeEventListener> list) {
		this.timer = timer;
		this.bootstrap = bootstrap;
		this.list = list;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		log.info("client:" + ctx.getName() + " message received");
		String buf = (String) e.getMessage();
		if (buf.equals(EventType.HEARTBEAT)) {
			System.out.println("[channel]" + e.getChannel()
					+ "[HEART_BEAT] received");
		}

		dataChanged(e.getMessage());
		e.getChannel().write(e.getMessage());
	}

	private void dataChanged(Object msg) {
		if (null == list) {
			log.error("list empty.");
			return;
		}

		for (DataChangeEventListener listener : list) {
			try {
				listener.dataChanged(msg);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		System.out.println("[channelClosed]" + e);
		timer.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				System.out.println("[Channel]" + " Reconnect to"
						+ getRemoteAddress());
				bootstrap.connect();
			}

		}, NettyClient.RECONNECT_DELAY, TimeUnit.SECONDS);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		bootstrap.setOption("CHANNEL", e.getChannel());
		e.getChannel().write("reconnection success");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		Throwable cause = e.getCause();
		if (cause instanceof ConnectException) {
			System.out.println("Failed to connect: " + cause.getMessage());
		}
		if (cause instanceof ReadTimeoutException) {
			System.out
					.println("Disconnecting due to no inbound traffic:readtimeout");
		} else if (cause instanceof WriteTimeoutException) {
			System.out
					.println("Disconnecting due to no inbound traffic:writetiemout");
		} else {
			System.out.println(cause);
		}
		e.getChannel().close();
	}

	private InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress) bootstrap.getOption("remoteAddress");
	}
}
