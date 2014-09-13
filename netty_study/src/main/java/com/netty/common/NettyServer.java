package com.netty.common;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class NettyServer implements Bootstrap {
	private final Log log = LogFactory.getLog(getClass());
	private ServerBootstrap bootstrap;
	private String host;
	private int port;

	public NettyServer(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("please input host, ip.");
			return;
		}
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		NettyServer server = new NettyServer(host, port);

		server.start();
	}

	public void start() {
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new BufferServerPipelineFactory());
		bootstrap.bind(new InetSocketAddress(host, port));
		log.info("server wait.");
	}

	public void stop() {
		if (null != bootstrap) {
			bootstrap.releaseExternalResources();
			log.info("server stop.");
		}
	}
}
