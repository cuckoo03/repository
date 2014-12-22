package com;

import java.nio.ByteBuffer;

public class Exam13_4BulkWriteTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuffer buf = ByteBuffer.allocate(10);
		buf.put((byte) 0).put((byte) 1).put((byte) 2).put((byte) 3)
		.put((byte) 4);
		buf.mark();
		System.out.println("position:" + buf.position() + ", limit:"
				+ buf.limit());

		byte[] b = new byte[5];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) i;
		}

		int size = buf.remaining();
		if (b.length < size) {
			size = b.length;
		}

		buf.put(b, 0, size);
		System.out.println("position:" + buf.position() + ", limit:"
				+ buf.limit());
		
		buf.reset();
		dosomething(buf, size);
	}
	public static void dosomething(ByteBuffer buf, int size) {
		for (int i = 0; i < size; i++) {
			System.out.println("byte=" + buf.get(i));
		}
	}

}
