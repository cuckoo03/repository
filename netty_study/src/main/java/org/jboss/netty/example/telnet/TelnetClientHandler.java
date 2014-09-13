package org.jboss.netty.example.telnet;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class TelnetClientHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger
			.getLogger(TelnetClientHandler.class.getName());

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
		System.err.println(e.getMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		System.out.println("server is bounded :" + e.getChannel().isBound());
		System.out.println("server is connected:" + e.getChannel().isConnected());
		System.out.println("server is opened:" + e.getChannel().isOpen());
		System.out.println("server is readabled:" + e.getChannel().isReadable());
		System.out.println("server is writable:" + e.getChannel().isWritable());
		
		logger.log(Level.WARNING, "unexcepected exception from downstream",
				e.getCause());
		e.getChannel().close();
	}
}
