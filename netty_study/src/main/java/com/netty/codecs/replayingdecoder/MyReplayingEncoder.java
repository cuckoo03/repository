package com.netty.codecs.replayingdecoder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class MyReplayingEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {

		if (msg instanceof Envelope) {
			return encodeMessage((Envelope) msg);
		} else {
			return msg;
		}
	}

	private Object encodeMessage(Envelope message) {
		int size = 6 + message.getPayload().length;

		ChannelBuffer buffer = ChannelBuffers.buffer(size);
		buffer.writeByte(message.getVersion().getByteValue());
		buffer.writeByte(message.getType().getByteValue());
		buffer.writeInt(message.getPayload().length);
		buffer.writeBytes(message.getPayload());
		return buffer;
	}
}
