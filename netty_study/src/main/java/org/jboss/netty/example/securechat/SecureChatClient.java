package org.jboss.netty.example.securechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class SecureChatClient {
	private static final String CARRAIGE_RETURN = "\r\n";
	private final String host;
	private final int port;

	public SecureChatClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	private void run() {
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new SecureChatClientPipelineFactory());

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));

		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}

		ChannelFuture lastWriteFuture = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			for (;;) {
				String line = in.readLine();
				if (null == line) {
					break;
				}

				lastWriteFuture = channel.write(line + CARRAIGE_RETURN);

				if ("bye".equals(line.toLowerCase())) {
					channel.getCloseFuture().awaitUninterruptibly();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// wait untill all message are flushed before closing the channel.
		if (null != lastWriteFuture) {
			lastWriteFuture.awaitUninterruptibly();
		}
		
		channel.close().awaitUninterruptibly();
		
		bootstrap.releaseExternalResources();

	}

	public static void main(String[] args) {
		/*
		if (args.length != 2) {
			System.err.println("Usage:"
					+ SecureChatClient.class.getSimpleName() + "<host> <port>");
			return;
		}

		String host = args[0];
		int port = Integer.parseInt(args[1]);
		*/
		String host = "127.0.0.1";
		int port = 8443;

		new SecureChatClient(host, port).run();
	}
}