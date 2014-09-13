package org.jboss.netty.example.objectecho;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ObjectEchoClientHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger
			.getLogger(ObjectEchoClientHandler.class.getName());

	private final List<Integer> firstMessage;

	private final AtomicLong transferredMessages = new AtomicLong();

	public ObjectEchoClientHandler(int firstMessageSize) {
		if (firstMessageSize <= 0) {
			throw new IllegalArgumentException("firstMessgaeSize:"
					+ firstMessageSize);
		}
		this.firstMessage = new ArrayList<Integer>(firstMessageSize);
		for (int i = 0; i < firstMessageSize; i++) {
			firstMessage.add(Integer.valueOf(i));
		}
	}

	public long getTransferredMessage() {
		return transferredMessages.get();
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent) {
			if (((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
				logger.info(e.toString());
			}
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		e.getChannel().write(firstMessage);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		transferredMessages.incrementAndGet();
		System.out.println("clientHandler:" + e.getMessage());
		e.getChannel().write((List<Integer>)e.getMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(Level.WARNING, "Unexpected exception from donwstream",
				e.getCause());
		e.getChannel().close();
	}
}