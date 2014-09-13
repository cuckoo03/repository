package org.jboss.netty.example.securechat;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.handler.ssl.SslHandler;

public class SecureChatServerHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger
			.getLogger(SecureChatServerHandler.class.getName());

	static final ChannelGroup channels = new DefaultChannelGroup();

	private static final String CARRAIGE_RETURN = "\n";

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		logger.info("handleUpstream");
		if (e instanceof ChannelStateEvent) {
			logger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		final SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);

		ChannelFuture handshakeFuture = sslHandler.handshake();
		handshakeFuture.addListener(new Greeter(sslHandler));
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) {
		logger.info("channelDisconncted");
		channels.remove(e.getChannel());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		String request = (String) e.getMessage();

		for (Channel c : channels) {
			if (c != e.getChannel()) {
				c.write("[" + e.getChannel().getRemoteAddress() + "]" + request
						+ CARRAIGE_RETURN);
			} else {
				c.write("[you] " + request + CARRAIGE_RETURN);
			}
		}

		if ("bye".equals(request.toLowerCase())) {
			e.getChannel().close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(Level.WARNING, "error", e.getCause());
		e.getChannel().close();
	}

	private static final class Greeter implements ChannelFutureListener {
		private final SslHandler sslHandler;

		public Greeter(SslHandler sslHandler) {
			this.sslHandler = sslHandler;
		}

		public void operationComplete(ChannelFuture future) throws Exception {
			if (future.isSuccess()) {
				future.getChannel().write(
						"Welcome to "
								+ InetAddress.getLocalHost().getHostName()
								+ " secure chat service" + CARRAIGE_RETURN);
				future.getChannel().write(
						"your session is protected by "
								+ sslHandler.getEngine().getSession()
										.getCipherSuite() + " cipher suite"
								+ CARRAIGE_RETURN);

				channels.add(future.getChannel());
			} else {
				future.getChannel().close();
			}
		}
	}
}