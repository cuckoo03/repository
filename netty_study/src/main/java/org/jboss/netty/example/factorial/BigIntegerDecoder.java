package org.jboss.netty.example.factorial;

import java.math.BigInteger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.CorruptedFrameException;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class BigIntegerDecoder extends FrameDecoder {
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() < 5) {
			return null;
		}

		buffer.markReaderIndex();

		int magicNumber = buffer.readUnsignedByte();
		if (magicNumber != 'F') {
			buffer.resetReaderIndex();
			throw new CorruptedFrameException("Iinvalid magic numberr:"
					+ magicNumber);
		}

		int dataLength = buffer.readInt();
		System.out.println("buffer readable:" + buffer.readableBytes());
		if (buffer.readableBytes() < dataLength) {
			buffer.resetReaderIndex();
			return null;
		}

		byte[] decoded = new byte[dataLength];
		buffer.readBytes(decoded);

		System.out.println(new BigInteger(decoded).intValue() + " : decode");
		return new BigInteger(decoded);
	}
}