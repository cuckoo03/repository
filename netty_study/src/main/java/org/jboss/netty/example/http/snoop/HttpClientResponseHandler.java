package org.jboss.netty.example.http.snoop;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.CharsetUtil;

public class HttpClientResponseHandler extends SimpleChannelUpstreamHandler {
	private boolean readingChunks;

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (!readingChunks) {
			HttpResponse response = (HttpResponse) e.getMessage();

			System.out.println("Status: " + response.getStatus());
			System.out.println("Version: " + response.getProtocolVersion());
			System.out.println();

			if (!response.getHeaderNames().isEmpty()) {
				for (String name : response.getHeaderNames()) {
					for (String value : response.getHeaders(name)) {
						System.out.println("header: " + name + "=" + value);
					}
				}
				System.out.println();
			}

			if (response.getStatus().getCode() == 200 && response.isChunked()) {
				readingChunks = true;
				System.out.println("Chunked content {");
			} else {
				ChannelBuffer content = response.getContent();
				if (content.readable()) {
					System.out.println("content {");
					System.out.println(content.toString(CharsetUtil.UTF_8));
					System.out.println("} end of content");
				}
			}
		} else {
			HttpChunk chunk = (HttpChunk) e.getMessage();
			if (chunk.isLast()) {
				readingChunks = false;
				System.out.println("} end of chunked content");
			} else {
				System.out.println(chunk.getContent().toString(
						CharsetUtil.UTF_8));
				System.out.flush();
			}
		}
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
	}
}