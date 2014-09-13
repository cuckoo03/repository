package org.jboss.netty.example.uptime2;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.example.BootstrapOptions;
import org.jboss.netty.example.uptime.UptimeClientHandler;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

public class IdleClient {
	private static final int READ_TIMEOUT = 2;
	private static final int WRITE_TIMEOUT = 2;
	public static final int RECONNECT_DELAY = 5;

	public static void main(String[] args) {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		final ClientBootstrap bootstrap = new ClientBootstrap(factory);
		final Timer timer = new HashedWheelTimer();

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			private final ChannelHandler idleStateHandler = new IdleStateHandler(
					timer,0, 2, 0);
			private final ChannelHandler uptimeHandler = new UptimeClientHandler(
					bootstrap, timer);

			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline p = Channels.pipeline();
				p.addLast("idle", idleStateHandler);
				p.addLast("heartbeat", new MyIdleStateHandler());
				p.addLast("handler2", uptimeHandler);
				p.addLast("handler1", new UptimeClientDownHandler());
				return p;
			}
		});

		bootstrap.setOption(BootstrapOptions.REMOTE_ADDRESS,
				new InetSocketAddress("127.0.0.1", 10001));

		bootstrap.connect();
	}
}

class UptimeClientDownHandler extends SimpleChannelDownstreamHandler {
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		System.out.println("writeRequested");
		// ReadTimeout, WriteTimeout exception 발생시키려 지연시킴
		// Thread.sleep(4000);
		ctx.sendDownstream(e);
	}
}