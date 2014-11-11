package org.jboss.netty.example.qotm;

import java.util.Random;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class QuoteOfTheMomentServerHandler extends SimpleChannelUpstreamHandler {

	private Random random = new Random();
	private static final String[] quotes = new String[] { "A", "B", "C", "D",
			"가", "나", "다", "라" };

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		String msg = (String) e.getMessage();
		if (msg.equals("QOTM?")) {
			e.getChannel().write("QOTM: " + nextQuote() + ",ip:" + e.getRemoteAddress(), e.getRemoteAddress());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		// don't close channel
	}

	private String nextQuote() {
		int quoteId;
		synchronized (random) {
			quoteId = random.nextInt(quotes.length);
		}
		return quotes[quoteId];
	}
}
