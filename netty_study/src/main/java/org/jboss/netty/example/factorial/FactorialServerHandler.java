package org.jboss.netty.example.factorial;

import java.math.BigInteger;
import java.util.Formatter;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class FactorialServerHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger
			.getLogger(FactorialServerHandler.class.getName());
	private int lastMultiplier;
	private BigInteger factorial = new BigInteger(new byte[] { 1 });

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
		System.out.println("messageReceived");
		BigInteger number;
		if (e.getMessage() instanceof BigInteger) {
			number = (BigInteger) e.getMessage();
		} else {
			number = new BigInteger(e.getMessage().toString());
		}
		lastMultiplier = number.intValue();
		factorial = factorial.multiply(number);
		e.getChannel().write(factorial);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) {
		logger.info(new Formatter().format("Factorial of %,d is: %,d",
				lastMultiplier, factorial).toString());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.warning(e.toString());
		e.getChannel().close();
	}
}
