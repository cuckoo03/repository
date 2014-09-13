package org.jboss.netty.example.time;

import java.nio.channels.Channels;
import java.util.Date;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class TimeClientHandler extends SimpleChannelHandler {
	private final ChannelBuffer buf = new DynamicChannelBuffer(10);

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		ChannelBuffer m = (ChannelBuffer) e.getMessage();
		buf.writeBytes(m);
		
		if (buf.readableBytes() >= 8) {
			long currentTimeMillis = buf.readInt() * 1000L;
			System.out.println(new Date(currentTimeMillis));
			System.out.println("TimeClientHandler channelBuffer close");
			e.getChannel().close();
		}
		else
		{
			System.out.println("buffer read wait:" + buf.readableBytes());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}
