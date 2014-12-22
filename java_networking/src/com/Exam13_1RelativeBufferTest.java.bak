package com;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class Exam13_1RelativeBufferTest {
	/**
	 * 상대적인 위치 읽고 쓰기
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuffer buf = ByteBuffer.allocate(10);
		System.out.println("init position:" + buf.position());
		System.out.println("init limit:" + buf.limit());
		System.out.println("init capacity:" + buf.capacity());
		
		buf.mark();
		
		buf.put((byte) 0).put((byte) 11).put((byte) 12);
		
		buf.reset();
		
		System.out.println("value:" + buf.get() +", position:" + buf.position());
		System.out.println("value:" + buf.get() +", position:" + buf.position());
		System.out.println("value:" + buf.get() +", position:" + buf.position());
		
		CharBuffer charBuf = CharBuffer.allocate(4);
		charBuf.mark();
		charBuf.put('ㅁ').put('a').put('ㅎ');
		charBuf.reset();
		System.out.println(charBuf.get());
		System.out.println(charBuf.get());
		System.out.println(charBuf.get());
	}

}
