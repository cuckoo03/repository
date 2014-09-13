package org.jboss.netty.example.http.upload2;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class HttpUploadServer {

	public static boolean isSSL;
	private final int port;

	public HttpUploadServer(int port) {
		this.port = port;
	}

	private void run() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new HttpUploadServerPipelineFactory());

		bootstrap.bind(new InetSocketAddress(port));
	}

	public static void main(String[] args) {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		if (args.length > 1) {
			isSSL = true;
		}
		new HttpUploadServer(port).run();
	}
}