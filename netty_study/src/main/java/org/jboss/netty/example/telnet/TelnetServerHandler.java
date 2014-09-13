package org.jboss.netty.example.telnet;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class TelnetServerHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger
			.getLogger(TelnetServerHandler.class.getName());

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent) {
			logger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		String request = (String) e.getMessage();

		String response;
		boolean close = false;
		if (request.length() == 0) {
			response = "Please type something.\r\n";
		} else if (request.toLowerCase().equals("bye")) {
			response = "Have a good day\r\n";
			close = true;
		} else {
			response = "Did you say " + request + "?\r\n";
			logger.info("server:" + response);
		}

		ChannelFuture future = e.getChannel().write(response);

		if (close) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		System.out.println("client is bounded :" + e.getChannel().isBound());
		System.out.println("client is connected:" + e.getChannel().isConnected());
		System.out.println("client is opened:" + e.getChannel().isOpen());
		System.out.println("client is readabled:" + e.getChannel().isReadable());
		System.out.println("client is writable:" + e.getChannel().isWritable());
		
		logger.log(Level.WARNING, "Unexpected exception from downstream",
				e.getCause());
		e.getChannel().close();
	}
}