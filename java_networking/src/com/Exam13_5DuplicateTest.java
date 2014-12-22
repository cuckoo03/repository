package com;

import java.nio.ByteBuffer;

public class Exam13_5DuplicateTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuffer buf = ByteBuffer.allocate(10);
		buf.put((byte) 0).put((byte) 1).put((byte) 2).put((byte) 3)
				.put((byte) 4).put((byte) 5).put((byte) 6).put((byte) 7)
				.put((byte) 8).put((byte) 9);
		buf.position(3);
		buf.limit(9);
		buf.mark();

		ByteBuffer buf2 = buf.duplicate();
		System.out.println("buf2 position:" + buf2.position() + ", buf2 limit:"
				+ buf2.limit() + ", buf2 capacity:" + buf2.capacity());

		buf2.position(7);
		buf2.reset();
		System.out.println("buf2 reset() position:" + buf2.position());

		buf2.clear();
		System.out.println("buf2 clear() position:" + buf2.position());

		System.out.print("buf2 remain: ");
		while (buf2.hasRemaining()) {
			System.out.print(buf2.get() + " ");
		}

		System.out.println("");
		System.out.println("Original buffer value:" + buf.get(0));
		System.out.println("dublication buffer value:" + buf2.get(0));

		buf.put(1, (byte) 11);
		System.out.println("buf2 1 -> 11");

		System.out.println("Original buffer value:" + buf.get(1));
		System.out.println("dublication buffer value:" + buf2.get(1));
	}
}
