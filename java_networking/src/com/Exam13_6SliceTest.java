package com;

import java.nio.ByteBuffer;

public class Exam13_6SliceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuffer buf = ByteBuffer.allocate(10);
		buf.put((byte) 0).put((byte) 1).put((byte) 2).put((byte) 3).put(
				(byte) 4).put((byte) 5).put((byte) 6).put((byte) 7).put(
				(byte) 8).put((byte) 9);
		
		buf.position(3);
		buf.limit(9);
		
		ByteBuffer buf2 = buf.slice();
		System.out.println("position:" + buf2.position() + ", limit:"
				+ buf2.limit() + ", capacity:" + buf2.capacity());
		
		while (buf2.hasRemaining()) {
			System.out.print(buf2.get() + " ");
		}
		
		System.out.println("");
		buf.put(3, (byte) 10);
		System.out.println("buf 3->10");
		
		System.out.println("Original buffer value:" + buf.get(3));
		System.out.println("dublication buffer value:" + buf2.get(0));
		
		buf.put(4, (byte) 11);
		System.out.println("buf 4->11");
		
		System.out.println("Original buffer value:" + buf.get(4));
		System.out.println("dublication buffer value:" + buf2.get(1));
	}

}
