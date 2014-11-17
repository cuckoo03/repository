package org.jboss.netty.example.idle2;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

public class IdleServer {
	public static void main(String[] args) {
		NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		final Timer timer = new HashedWheelTimer();
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			final IdleStateHandler idleStateHandler = new IdleStateHandler(
					timer, 5, 3, 0);

			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline p = Channels.pipeline();
				p.addLast("idle", idleStateHandler);
				p.addLast("heartbeat", new MyIdleStateHandler());
				return p;
			}
		});
		bootstrap.bind(new InetSocketAddress(9001));
		System.out.println("server bind");
	}
}

class MyIdleStateHandler extends IdleStateAwareChannelHandler {
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws InterruptedException {
		System.out.println("server messageReceived");
		 Thread.sleep(6000);
		/**
		 * No data was received for a while.
		 */
		// 일정시간동안 데이터를 받지못할 경우 reader idle 상태가 된다.
	}

	/**
	 * 반드시 sendDownstream을 호출해야 함
	 */
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws InterruptedException {
		System.out.println("server writeRequested");
		/**
		 * No data was sent for a while.
		 */
		// 일정시간동안 데이터를 보내지않을 경우 writer idle 상태가 된다.
		Thread.sleep(4000);
		ctx.sendDownstream(e);
	}

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) {
		if (e.getState() == IdleState.READER_IDLE) {
			System.out.println("Reader idle, closing channel");
			// e.getChannel().close();
			e.getChannel().write(
					ChannelBuffers.copiedBuffer("heartbeat-reader_idle"
							.getBytes()));
		} else if (e.getState() == IdleState.WRITER_IDLE) {
			System.out.println("Writer idle, sending heartbeat");
			// e.getChannel().write("heartbeat-writer_idle");
		} else if (e.getState() == IdleState.ALL_IDLE) {
			System.out.println("All idle, sending heartbeat");
			// e.getChannel().write("heartbeat-all_idle");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}
