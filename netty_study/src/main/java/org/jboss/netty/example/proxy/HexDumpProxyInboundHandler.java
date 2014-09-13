package org.jboss.netty.example.proxy;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.handler.traffic.TrafficCounter;

public class HexDumpProxyInboundHandler extends SimpleChannelUpstreamHandler {

	private final ClientSocketChannelFactory cf;
	private final String remoteHost;
	private final int remotePort;
	private volatile Channel outboundChannel;
	private final Object trafficLock = new Object();

	public HexDumpProxyInboundHandler(ClientSocketChannelFactory cf,
			String remoteHost, int remotePort) {
		this.cf = cf;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
		final Channel inboundChannel = e.getChannel();
		inboundChannel.setReadable(false);

		ClientBootstrap cb = new ClientBootstrap(cf);
		cb.getPipeline()
				.addLast("handler", new OutboundHandler(inboundChannel));
		ChannelFuture f = cb.connect(new InetSocketAddress(remoteHost,
				remotePort));

		outboundChannel = f.getChannel();
		f.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future)
					throws Exception {
				if (future.isSuccess()) {
					System.out.println("proxy : Real server connect");
					// connection 후에 incomming traffiic을 accept한다.
					inboundChannel.setReadable(true);
				} else {
					inboundChannel.close();
				}
			}
		});
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		ChannelBuffer msg = (ChannelBuffer) e.getMessage();
		System.out.println(">>>" + ChannelBuffers.hexDump(msg));

		synchronized (trafficLock) {
			outboundChannel.write(msg);

			if (!outboundChannel.isWritable()) {
				e.getChannel().setReadable(false);
			}
		}
	}

	@Override
	public void channelInterestChanged(ChannelHandlerContext ctx,
			ChannelStateEvent e) {
		synchronized (trafficLock) {
			if (e.getChannel().isWritable()) {
				if (null != outboundChannel) {
					outboundChannel.setReadable(true);
				}
			}
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		if (null != outboundChannel) {
			closeOnFlush(outboundChannel);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		closeOnFlush(e.getChannel());
	}

	private class OutboundHandler extends SimpleChannelUpstreamHandler {
		private final Channel inboundChannel;

		public OutboundHandler(Channel inboundChannel) {
			this.inboundChannel = inboundChannel;
		}

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
			ChannelBuffer msg = (ChannelBuffer) e.getMessage();

			synchronized (trafficLock) {
				inboundChannel.write(msg);

				if (!inboundChannel.isWritable()) {
					e.getChannel().setReadable(false);
				}
			}
		}

		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
			closeOnFlush(inboundChannel);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
			e.getCause().printStackTrace();
			closeOnFlush(e.getChannel());
		}
	}

	private void closeOnFlush(Channel ch) {
		if (ch.isConnected()) {
			ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(
					ChannelFutureListener.CLOSE);
		}
	}
}