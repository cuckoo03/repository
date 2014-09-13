package org.jboss.netty.example.decoder.onetoone;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.example.decoder.MyClassProtocol;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

public class MyOneToOneDecoder extends OneToOneDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		ChannelBuffer buf = (ChannelBuffer) msg;

		if (buf.readableBytes() < 8) {
			return null;
		}
		
		int id = buf.readInt();
		
		int dataLength = buf.readInt();
		byte[] decoded = new byte[dataLength];
		buf.readBytes(decoded);
		
		MyClassProtocol p = new MyClassProtocol(0, "");
		p.setId(id);
		p.setName(new String(decoded));

		return p;
	}
}