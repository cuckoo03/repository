package org.jboss.netty.example.factorial;

import java.math.BigInteger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class NumberEncoder extends OneToOneEncoder {
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof Number)) {
			return msg;
		}

		BigInteger v;
		if (msg instanceof BigInteger) {
			v = (BigInteger) msg;
		} else {
			v = new BigInteger(String.valueOf(msg));
		}

		byte[] data = v.toByteArray();
		int dataLength = data.length;

		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeByte('F');
		buf.writeInt(dataLength);
		buf.writeBytes(data);

		System.out.println(v.longValue() + " : encode");
		return buf;
	}
}