package org.jboss.netty.example.uptime;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelEvent;
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
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.handler.timeout.WriteTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

/**
 * readtimeout 기능은 동작하지만 writetimeout은 동작하지 않음
 * 
 * @author cuckoo03
 * 
 */
public class UptimeClient {
	private static final int READ_TIMEOUT = 2;
	private static final int WRITE_TIMEOUT = 1;
	public static final int RECONNECT_DELAY = 5;

	public static void main(String[] args) {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		final ClientBootstrap bootstrap = new ClientBootstrap(factory);
		final Timer timer = new HashedWheelTimer();

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			private final ChannelHandler readTimeoutHandler = new ReadTimeoutHandler(
					timer, READ_TIMEOUT, TimeUnit.SECONDS);
			private final ChannelHandler writeTimeoutHandler = new WriteTimeoutHandler(
					timer, WRITE_TIMEOUT, TimeUnit.SECONDS);
			private final ChannelHandler uptimeHandler = new UptimeClientHandler(
					bootstrap, timer);

			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline p = Channels.pipeline();
//				p.addLast("readtimeout", readTimeoutHandler);
				p.addLast("handler", uptimeHandler);
				p.addLast("down", new SimpleChannelDownstreamHandler(){
					public void writeRequested(ChannelHandlerContext ctx,
							MessageEvent e) throws Exception {
						System.out.println("writeRequested");
						System.out.println("");
						ctx.sendDownstream(e);
					}
				});
				p.addLast("delay", new SimpleChannelDownstreamHandler(){
					public void writeRequested(ChannelHandlerContext ctx,
							MessageEvent e) throws Exception {
						System.out.println("sleep");
						Thread.sleep(4000);
						ctx.sendDownstream(e);
					}
				});
				p.addLast("writeTimeOut", writeTimeoutHandler);
				return p;
			}
		});

		bootstrap.setOption(BootstrapOptions.REMOTE_ADDRESS,
				new InetSocketAddress("127.0.0.1", 9001));

		bootstrap.connect();
	}
}
