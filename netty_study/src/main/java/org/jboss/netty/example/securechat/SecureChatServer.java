package org.jboss.netty.example.securechat;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class SecureChatServer {
	private final int port;

	public SecureChatServer(int port) {
		this.port = port;
	}

	private void run() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new SecureChatServerPipelineFactory());

		bootstrap.bind(new InetSocketAddress(port));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8443;
		}
		new SecureChatServer(port).run();
	}
}
