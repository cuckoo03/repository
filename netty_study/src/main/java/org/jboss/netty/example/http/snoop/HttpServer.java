package org.jboss.netty.example.http.snoop;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class HttpServer {
	public static void main(String[] args) {
		final boolean isSsl = true;
		int port = 0;
		if (isSsl) {
			port = 443;
		} else {
			port = 8080;
		}

		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new HttpServerPipelineFactory(isSsl));

		bootstrap.bind(new InetSocketAddress(port));
		System.err.println("Server bind");
	}
}