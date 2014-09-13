package org.jboss.netty.example.codecs.onetoone;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.codecs.decoder.MyClassProtocol;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class MyOneToOneEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof MyClassProtocol)) {
			return null;
		}
		MyClassProtocol s = (MyClassProtocol) msg;

		// packet:|id|name length|name bytes|
		 ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		 buf.writeInt(s.getId());
		 buf.writeInt(s.getName().getBytes().length);
		 buf.writeBytes(s.getName().getBytes());
		return buf;
	}
}