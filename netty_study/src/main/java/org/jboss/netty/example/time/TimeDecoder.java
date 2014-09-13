package org.jboss.netty.example.time;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class TimeDecoder extends FrameDecoder {
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		System.out.println("decode");

		if (buffer.readableBytes() < 8) {
			return null;
		}

		System.out.println("buffer read complete");
		return new UnixTime(buffer.readLong());
	}

}
