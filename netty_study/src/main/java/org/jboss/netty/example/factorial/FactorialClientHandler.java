package org.jboss.netty.example.factorial;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class FactorialClientHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger
			.getLogger(FactorialClientHandler.class.getName());
	private final int count;
	private int receiveMessage = 0;
	private int i = 1;
	final BlockingQueue<BigInteger> answer = new LinkedBlockingQueue<BigInteger>();

	public FactorialClientHandler(int count) {
		this.count = count;
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent) {
			logger.info(e.toString());
		}

		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		sendNumber(e);
	}

	@Override
	public void channelInterestChanged(ChannelHandlerContext xtx,
			ChannelStateEvent e) {
//		sendNumber(e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) {
		System.out.println("messageReceived");
		receiveMessage++;
		if (receiveMessage == count) {
			e.getChannel().close().addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future)
						throws Exception {
					boolean offered = answer.offer((BigInteger) e.getMessage());
					assert offered;
				}
			});
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.warning("unexcepted exception from donwstream");
		e.getChannel().close();
	}

	public BigInteger getFactorial() {
		boolean interrupted = false;
		for (;;) {
			try {
				BigInteger factorial = answer.take();
				if (interrupted) {
					Thread.currentThread().interrupt();
				}
				return factorial;
			} catch (InterruptedException e) {
				interrupted = true;
				logger.warning(e.toString());
				logger.warning("");
			}
		}
	}

	private void sendNumber(ChannelStateEvent e) {
		Channel channel = e.getChannel();
		while (/*channel.isWritable()*/true) {
			if (i <= count) {
				channel.write(Integer.valueOf(i));
				System.out.println("sendNumber");
				i++;
			} else {
				break;
			}
		}
	}
}