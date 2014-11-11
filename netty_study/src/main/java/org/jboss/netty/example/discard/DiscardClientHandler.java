package org.jboss.netty.example.discard;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;

public class DiscardClientHandler extends SimpleChannelHandler {
	private final byte[] content;
	private final AtomicLong transferredBytes = new AtomicLong();

	public DiscardClientHandler(int messageSize) {
		if (messageSize < 0) {
			throw new IllegalArgumentException("messageSize:" + messageSize);
		}

		content = new byte[messageSize];
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		System.out.println("handleUpstream");
		if (e instanceof ChannelStateEvent) {
			if (((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
				System.out.println(e.toString());
			}
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		try {
			System.out.println("channelConnected");
			generateTraffic(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void channelInterestChanged(ChannelHandlerContext ctx,
			ChannelStateEvent e) {
		try {
			System.out.println("channelInterestChanged");
			generateTraffic(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		System.out.println("messageReceived");
	}

	@Override
	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) {
		System.out.println("writecomplete");
		transferredBytes.addAndGet(e.getWrittenAmount());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		System.out.println(e.getCause());
		e.getChannel().close();
	}

	private void generateTraffic(ChannelStateEvent e)
			throws InterruptedException {
		Channel channel = e.getChannel();
		while (channel.isWritable()) {
			ChannelBuffer m = nextMessage();
			if (null == m) {
				break;
			}
			Thread.sleep(2000);
			channel.write(m);
		}
		System.out.println("can't writable");
	}

	private ChannelBuffer nextMessage() {
		return ChannelBuffers.wrappedBuffer(content);
	}
}