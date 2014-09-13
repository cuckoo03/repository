package com.netty.common;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.example.BootstrapOptions;

import com.netty.buffer.DataObserver;

public class NettyClient implements Bootstrap {
	private final Log log = LogFactory.getLog(getClass());
	private ClientBootstrap bootstrap;
	private ChannelFuture future;
	private final String host;
	private int port;
	private List<DataChangeEventListener> list = new ArrayList<>();

	public NettyClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new BufferClientPipelineFactory(list));
		bootstrap.setOption(BootstrapOptions.REMOTE_ADDRESS,
				new InetSocketAddress(host, port));
		bootstrap.setOption(BootstrapOptions.CONNECT_TIMEOUT_MILLIS, 3000);

		future = bootstrap.connect().awaitUninterruptibly();

		if (!future.isSuccess()) {
			log.error("connection failed.");
			future.getChannel().close().awaitUninterruptibly();
			bootstrap.releaseExternalResources();
			return;
		}
		log.info("connected server.");
		
//		future.getChannel().getCloseFuture().awaitUninterruptibly();
//		bootstrap.releaseExternalResources();
	}

	public void stop() {
		Channel channel = getChannel(bootstrap);
		channel.close();
		bootstrap.releaseExternalResources();
	}

	public void send() {
		ChannelBuffer heapBuf = ChannelBuffers.buffer(1);
		heapBuf.writeByte('a');
		future.getChannel().write(heapBuf);
	}

	public void addEventListener(DataChangeEventListener listener) {
		list.add(listener);
	}

	private Channel getChannel(ClientBootstrap bootstrap) {
		Channel channel = (Channel) bootstrap.getOption("CHANNEL");
		return channel;
	}
}