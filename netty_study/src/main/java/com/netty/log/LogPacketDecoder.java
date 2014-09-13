package com.netty.log;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import com.netty.log.vo.MyPacket;

public class LogPacketDecoder extends OneToOneDecoder {
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof ChannelBuffer)) {
			throw new Exception("decode failed.");
		}
		
		ChannelBuffer buf = (ChannelBuffer) msg;
		if (buf.readableBytes() < 11) {
			return null;
		}
		
		buf.markReaderIndex();
		
		long clientId = buf.readLong();
		int dataLength = buf.readInt();
		byte[] data = new byte[dataLength];
		buf.readBytes(data);
		
		MyPacket p = new MyPacket();
		p.setClientId(clientId);
		p.setReceiveData(new String(data));
		
		return p;
	}
}
