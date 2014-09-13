package org.jboss.netty.example.time;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class TimeEncoder extends SimpleChannelHandler {
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {
		System.out.println("writeRequested");
		UnixTime time = (UnixTime) e.getMessage();

		ChannelBuffer buf = ChannelBuffers.buffer(8);
		buf.writeLong(time.getValue());

		Channels.write(ctx, e.getFuture(), buf);
	}
}
