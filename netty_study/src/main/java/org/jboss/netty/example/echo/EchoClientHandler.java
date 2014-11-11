package org.jboss.netty.example.echo;

import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class EchoClientHandler extends SimpleChannelUpstreamHandler {
	private final ChannelBuffer firstMessage;
	private final AtomicLong transferredBytes = new AtomicLong();

	public EchoClientHandler(int firstMessageSize) {
		this.firstMessage = ChannelBuffers.buffer(firstMessageSize);
		for (int i = 0; i < firstMessage.capacity(); i++) {
			firstMessage.writeByte((byte) i);
		}
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		e.getChannel().write(firstMessage);
		System.out.println("client : channelConnected : " + firstMessage);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Long count = transferredBytes
				.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes());
		System.out.println("client : messageReceived " + count);
		e.getChannel().write(e.getMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}