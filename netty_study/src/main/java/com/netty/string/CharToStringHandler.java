package com.netty.string;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferIndexFinder;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class CharToStringHandler extends SimpleChannelUpstreamHandler {
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		System.out.println(e.getMessage());
		// String msg = (String) e.getMessage();
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();
		int nullIndex = buf.indexOf(0, 30, ChannelBufferIndexFinder.NUL);
		@SuppressWarnings("deprecation")
		String result = buf.toString(0, nullIndex, "US-ASCII");
		System.out.println(result);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}
