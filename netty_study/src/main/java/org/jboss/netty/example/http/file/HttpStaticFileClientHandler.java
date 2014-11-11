package org.jboss.netty.example.http.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.example.http.upload.HttpUploadClient;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.CharsetUtil;

public class HttpStaticFileClientHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger
			.getLogger(HttpUploadClient.class.getSimpleName());

	private boolean readingChunks;

	private FileOutputStream fos;

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		try {
			fos = new FileOutputStream("HttpFileDownload.pdf");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		System.out.println("messageReceived chunks:" + readingChunks);
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

			if (response.isChunked()) {
				readingChunks = true;
				logger.info("chunked content {");
			} else {
				ChannelBuffer content = response.getContent();
				if (content.readable()) {
					logger.info("content {");
//					logger.info(content.toString(CharsetUtil.UTF_8));
					logger.info("} end of content");
					if (content.readable()) {
						byte[] b = new byte[content.readableBytes()];
						try {
							content.readBytes(b);
							fos.write(b);
							fos.flush();
							fos.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		} else {
			//chunked
			HttpChunk chunk = (HttpChunk) e.getMessage();
			if (chunk.isLast()) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				readingChunks = false;
				logger.info("} end of chunked content");
			} else {
				// keep reading chunk
//				logger.info(chunk.getContent().toString(CharsetUtil.UTF_8));
				ChannelBuffer buf = chunk.getContent();
				try {
					if (buf.readable()) {
						byte[] b = new byte[buf.readableBytes()];
						buf.readBytes(b);
						fos.write(b);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}