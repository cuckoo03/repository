package com.netty.common;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.example.BootstrapOptions;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

public class NettyClient implements Bootstrap {
	public static final int RECONNECT_DELAY = 5;
	private final Log log = LogFactory.getLog(getClass());
	private ClientBootstrap bootstrap;
	private ChannelFuture future;
	private final String host;
	private int port;
	private List<DataChangeEventListener> list = new ArrayList<>();
	private Timer timer;

	public NettyClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		bootstrap = new ClientBootstrap(factory);

		timer = new HashedWheelTimer();
		bootstrap.setPipelineFactory(new BufferClientPipelineFactory(list,
				timer, bootstrap));
		bootstrap.setOption(BootstrapOptions.REMOTE_ADDRESS,
				new InetSocketAddress(host, port));
		bootstrap.setOption(BootstrapOptions.CONNECT_TIMEOUT_MILLIS, 3000);
		bootstrap
		.setOption("remoteAddress", new InetSocketAddress(host, port));
		
		future = bootstrap.connect().awaitUninterruptibly();

		log.info("connected server.");
	}

	public void stop() {
		// Channel channel = getChannel(bootstrap);
		// channel.close();
		bootstrap.releaseExternalResources();
	}

	public void send(String msg) {
		// ChannelBuffer heapBuf = ChannelBuffers.buffer(1);
		future.getChannel().write(msg);
	}

	public void addEventListener(DataChangeEventListener listener) {
		list.add(listener);
	}

	private Channel getChannel(ClientBootstrap bootstrap) {
		Channel channel = (Channel) bootstrap.getOption("CHANNEL");
		return channel;
	}
}