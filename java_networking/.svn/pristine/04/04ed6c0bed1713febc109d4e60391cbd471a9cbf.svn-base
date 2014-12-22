package com;

import java.nio.ByteBuffer;

public class BufferUtil {
	public static String byteBufferToString(ByteBuffer buf) {
		byte[] data = new byte[buf.limit()];
		buf.get(data, 0, buf.limit());
		return new String(data);
	}
}
