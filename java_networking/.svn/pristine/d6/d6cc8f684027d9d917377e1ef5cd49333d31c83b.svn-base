package com;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

public class Exam13_7ViewBufferTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuffer buf = ByteBuffer.allocate(10);
		IntBuffer ib = buf.asIntBuffer();
		System.out.println("int buffer position:" + ib.position() + ", limit" + ib.limit()
				+ ", capacity:" + ib.capacity());

		ib.put(1024).put(2048);
		System.out.println("idx0:" + ib.get(0) + ", idx1:" + ib.get(1));

		while (buf.hasRemaining()) {
			System.out.print(buf.get() + " ");
		}
		System.out.println("");
		System.out.println("bytebuffer position:" + buf.position() + ", limit" + buf.limit()
				+ ", capacity:" + buf.capacity());
		System.out.println("int buffer position:" + ib.position() + ", limit"
				+ ib.limit() + ", capacity:" + ib.capacity());
		buf.clear();

		CharBuffer charBuf = buf.asCharBuffer();
		charBuf.put('a').put('b');
		System.out.println("charBuf position:" + charBuf.position()
				+ ", limit:" + charBuf.limit() + ", capacity:"
				+ charBuf.capacity());
	}

}
