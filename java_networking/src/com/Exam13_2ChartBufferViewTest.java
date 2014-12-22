package com;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class Exam13_2ChartBufferViewTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ByteBuffer buf = ByteBuffer.allocate(100);
		CharBuffer cbuf = buf.asCharBuffer();
		cbuf.put("Hello World!");
		cbuf.flip();
		
		String s = cbuf.toString();
		System.out.println("data:" + s);
		System.out.println("buffer position:" + cbuf.position());
		
		int start = 6;
		int end = 12;
		CharSequence sub = cbuf.subSequence(start, end);
		System.out.println(sub.toString());
	}

}
