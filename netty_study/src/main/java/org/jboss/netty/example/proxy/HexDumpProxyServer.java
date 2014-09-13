package org.jboss.netty.example.proxy;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class HexDumpProxyServer {
	private final int localPort;
	private final String remoteHost;
	private final int remotePort;

	public HexDumpProxyServer(int localPort, String remoteHost, int remotePort) {
		this.localPort = localPort;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
	}

	private void run() {
		System.err.println("Proxing *:" + localPort + " to " + remoteHost
				+ " : " + remotePort);

		Executor executor = Executors.newCachedThreadPool();
		ServerBootstrap sb = new ServerBootstrap(
				new NioServerSocketChannelFactory(executor, executor));

		ClientSocketChannelFactory cf = new NioClientSocketChannelFactory(
				executor, executor);

		sb.setPipelineFactory(new HexDumpProxyPipelineFactory(cf, remoteHost,
				remotePort));

		sb.bind(new InetSocketAddress(localPort));
	}

	public static void main(String[] args) throws Exception {
		/*
		if (args.length != 3) {
			System.err.println("Usage: " + HexDumpProxy.class.getSimpleName()
					+ "<local port> <remote host> <remote port>");
			return;
		}

		int localPort = Integer.parseInt(args[0]);
		String remoteHost = args[1];
		int remotePort = Integer.parseInt(args[2]);
		*/
		int localPort = 8080;
		String remoteHost = "127.0.0.1";
		int remotePort = 8081;

		new HexDumpProxyServer(localPort, remoteHost, remotePort).run();
	}
}