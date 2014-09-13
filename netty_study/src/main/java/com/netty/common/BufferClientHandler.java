package com.netty.common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class BufferClientHandler extends SimpleChannelUpstreamHandler {
	private Log log = LogFactory.getLog(getClass());
	private final List<DataChangeEventListener> list;

	public BufferClientHandler(List<DataChangeEventListener> list) {
		this.list = list;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		log.info("client:" + ctx.getName() + " message received");
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();
		if (!buf.readable()) {
			log.info("not readable");
			return;
		}
		/*
		byte[] b = new byte[buf.readableBytes()];
		buf.readBytes(b);
		System.out.println(new String(b));

		ChannelBuffer header = ChannelBuffers.dynamicBuffer(12);
		byte[] start = new byte[3];
		byte[] message = new byte[3];
		header.markReaderIndex();
		header.writeBytes("Yuu123".getBytes());
		header.markWriterIndex();
		System.out.println(header);

		header.readBytes(start);
		System.out.println(new String(start));
		System.out.println(header);

		header.readBytes(message);
		System.out.println(message);

		ChannelBuffer body = ChannelBuffers.wrappedBuffer(start, message);
		System.out.println("body:" + body);

		byte[] bb = new byte[body.readableBytes()];
		body.readBytes(bb);
		System.out.println(new String(bb));
		*/

		dataChanged();
		/*
		ChannelFuture future = e.getChannel().write(
				ChannelBuffers.dynamicBuffer());
		future.addListener(ChannelFutureListener.CLOSE);
		*/
	}

	private void dataChanged() {
		if (null == list) {
			log.error("list empty.");
			return;
		}
		
		for (DataChangeEventListener listener : list) {
			try {
				listener.dataChanged();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		log.error("unexpected exception." + e.getCause());
		e.getChannel().close();
	}
}
