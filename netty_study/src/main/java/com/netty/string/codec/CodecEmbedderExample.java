package com.netty.string.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.jboss.netty.handler.codec.embedder.EncoderEmbedder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class CodecEmbedderExample {

	/**
	 * codec을 테스트하는 예제
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ChannelBuffer buf = ChannelBuffers.wrappedBuffer("Hello World"
				.getBytes());

		EncoderEmbedder<ChannelBuffer> encoder = new EncoderEmbedder<ChannelBuffer>(
				new StringEncoder());
		encoder.offer("Hello World");

		ChannelBuffer encoded = encoder.poll();
		byte[] dst = new byte[encoded.readableBytes()];
		encoded.readBytes(dst);
		System.out.println(new String(dst));

		DecoderEmbedder<String> e = new DecoderEmbedder<String>(
				new StringDecoder());
		e.offer(buf);

		String decoded = e.poll();
		System.out.println(decoded);
	}
}
