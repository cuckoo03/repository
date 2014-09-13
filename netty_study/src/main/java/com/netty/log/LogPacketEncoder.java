package com.netty.log;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.netty.log.vo.MyPacket;

public class LogPacketEncoder extends OneToOneEncoder{

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof MyPacket)) {
			throw new Exception("decode failed.");
		}
		
		MyPacket p = (MyPacket) msg;
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeLong(p.getClientId());
		buf.writeInt(p.getReceiveDataLength());
		buf.writeBytes(p.getReceiveData().getBytes());
		
		return p;
	}

}
