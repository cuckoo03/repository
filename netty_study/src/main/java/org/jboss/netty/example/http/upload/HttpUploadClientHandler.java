package org.jboss.netty.example.http.upload;

import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.CharsetUtil;

public class HttpUploadClientHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger
			.getLogger(HttpUploadClient.class.getSimpleName());

	private boolean readingChunks;

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (!readingChunks) {
			HttpResponse response = (HttpResponse) e.getMessage();

			logger.info("Status:" + response.getStatus());
			logger.info("Version:" + response.getProtocolVersion());

			if (!response.getHeaders().isEmpty()) {
				for (String name : response.getHeaderNames()) {
					for (String value : response.getHeaders(name)) {
						logger.info("header: " + name + "=" + value);
					}
				}
			}

			if (response.getStatus().getCode() == 200 && response.isChunked()) {
				readingChunks = true;
				logger.info("chunked content {");
			} else {
				ChannelBuffer content = response.getContent();
				if (content.readable()) {
					logger.info("content {");
					logger.info(content.toString(CharsetUtil.UTF_8));
					logger.info("} end of content");
				}
			}
		} else {
			HttpChunk chunk = (HttpChunk) e.getMessage();
			if (chunk.isLast()) {
				readingChunks = false;
				logger.info("} end of chunked content");
			} else {
				logger.info(chunk.getContent().toString(CharsetUtil.UTF_8));
			}
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}