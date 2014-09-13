package org.jboss.netty.example.echo;

import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class EchoServerHandler extends SimpleChannelUpstreamHandler {
	private final AtomicLong transferredBytes = new AtomicLong();

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		long count = transferredBytes.addAndGet(((ChannelBuffer) e.getMessage())
				.readableBytes());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		e.getChannel().write(e.getMessage());
		System.out.println("server : messageReceived " + count);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}
