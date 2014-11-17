package com.netty.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class BufferServerHandler extends IdleStateAwareChannelUpstreamHandler {
	private Log log = LogFactory.getLog(getClass());
	private final Map<String, Channel> channelMap;

	public BufferServerHandler(Map<String, Channel> channelMap) {
		 this.channelMap = channelMap;
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		log.info("server:channel connected");
		addChannel(ctx.getChannel());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) {
		log.info("server:channel disconnected.");
		removeChannel(ctx.getChannel());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		log.info("server:messageReceived :");
		broadcast((String) e.getMessage());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
	}

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) {
		if (e.getState() == IdleState.READER_IDLE) {
			e.getChannel().close();
		} else if (e.getState() == IdleState.WRITER_IDLE) {
			e.getChannel().write("HeatBeat");
		} else if (e.getState() == IdleState.ALL_IDLE) {
		}
	}

	private void addChannel(Channel channel) {
		synchronized (channelMap) {
			channelMap.put(channel.getId() + "", channel);
			log.info("add [channelId]" + channel);
		}
	}

	private void removeChannel(Channel channel) {
		synchronized (channelMap) {
			channelMap.remove(channel.getId() + "");
			log.info("remove [channelId]" + channel);
		}
	}

	private void broadcast(String message) {
		synchronized (channelMap) {
			Set<String> keySet = channelMap.keySet();
			Iterator<String> iter = keySet.iterator();
			while (iter.hasNext()) {
				String id = iter.next();
				Channel channel = channelMap.get(id);
				if (channel.isConnected()) {
					channel.write(channel.getId() + ":" + message);
					log.info("write to [channel] " + channel);
				} else {
					log.info("can't write to [channel] " + channel);
				}
			}
		}
	}
}
