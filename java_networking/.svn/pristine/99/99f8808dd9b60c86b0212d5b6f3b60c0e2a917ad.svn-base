package com;

import java.nio.ByteBuffer;

public class Exam13_RelativeBufferTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuffer buf = ByteBuffer.allocate(10);
		System.out.println("init position:" + buf.position());
		System.out.println("init limit:" + buf.limit());
		System.out.println("init capacity:" + buf.capacity());

		buf.mark();
		buf.put((byte) 10).put((byte) 11).put((byte) 12);
		buf.reset();
		
		System.out.println(buf.get() + ":" + buf.position());
		System.out.println(buf.get() + ":" + buf.position());
		System.out.println(buf.get() + ":" + buf.position());
		
		System.out.println(buf.get() + ":" + buf.position());
	}

}
