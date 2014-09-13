package org.jboss.netty.example.decoder.fieldbaseddecoder;

import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(XmlHandler.class);
	private final AtomicLong transferredBytes = new AtomicLong();

	public XmlHandler() {
		logger.info("public XmlServerHandler( )");
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out
				.println("channelConnected( ChannelHandlerContext ctx , ChannelStateEvent e ) throws Exception");
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out
				.println("channelClosed( ChannelHandlerContext ctx , ChannelStateEvent e ) throws Exception");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		String request = (String) e.getMessage();
		transferredBytes.addAndGet(request.length());

		System.out.println(request);
		System.out.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.error("Unexpected exception from downstream. {} ", e.getCause());
		e.getChannel().close();
	}

	public long getTransferredBytes() {
		return transferredBytes.get();
	}

}