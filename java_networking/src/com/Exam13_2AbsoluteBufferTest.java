package com;

import java.nio.ByteBuffer;

public class Exam13_2AbsoluteBufferTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuffer buf = ByteBuffer.allocate(10);
		System.out.println("init position:" + buf.position());
		System.out.println("init limit:" + buf.limit());
		System.out.println("init capacity:" + buf.capacity());
		
		buf.put(3, (byte) 3);
		buf.put(4, (byte) 4);
		buf.put(5, (byte) 5);
		
		System.out.println("position:" + buf.position());
		
		System.out.println(buf.get(3) + ":" + buf.position());
		System.out.println(buf.get(4) + ":" + buf.position());
		System.out.println(buf.get(5) + ":" + buf.position());
		
		System.out.println(buf.get(9) + ":" + buf.position());
	}

}
