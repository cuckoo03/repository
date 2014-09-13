package com.netty.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class BufferServerHandler extends SimpleChannelUpstreamHandler {
	private Log log = LogFactory.getLog(getClass());
	private Map<String, Channel> channelMap = new HashMap<>();

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
		// e.getChannel().write(e.getMessage());
		broadcast((ChannelBuffer) e.getMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
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

	private void broadcast(ChannelBuffer message) {
		synchronized (channelMap) {
			Set<String> keySet = channelMap.keySet();
			Iterator<String> iter = keySet.iterator();
			while (iter.hasNext()) {
				String id = iter.next();
				Channel channel = channelMap.get(id);
				if (channel.isConnected()) {
					channel.write(message);
					log.info("write to [channel] " + channel);
				} else {
					log.info("can't write to [channel] " + channel);
				}
			}
		}
	}
}
