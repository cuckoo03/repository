package org.jboss.netty.example.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class TelnetClient {
	/**
	 * 클라이언트가 입력받은 메시지를 서버에서 받아 그대로 클라이언트에게 전송하는 예제
	 * Custom StringEncoder를 쓸 경우에는 한글이 깨지지 않게 처리하는 기능이 필요함.
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		args = new String[]{"127.0.0.1", "9001"};
		String host = null;
		int port = 0;
		if (args.length != 2) {
			System.err.println("Usage:" + TelnetClient.class.getSimpleName()
					+ "<host> <port>");
		} else {
			host = args[0];
			port = Integer.parseInt(args[1]);
		}

		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new TelnetClientPipelineFactory());

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));

		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}
		System.err.println("client connected");

		ChannelFuture lastWriteFuture = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			for (;;) {
				String line = in.readLine();
				if (null == line) {
					break;
				}

				lastWriteFuture = channel.write(line + "\r\n");

				System.out.println("client channel isSuccess:"
						+ lastWriteFuture.isSuccess());
				System.out.println("client channel isDone:"
						+ lastWriteFuture.isDone());
				System.out.println("client channel iscancel:"
						+ lastWriteFuture.isCancelled());
				if (line.toLowerCase().equals("bye")) {
					channel.getCloseFuture().awaitUninterruptibly();
					break;
				}
			}
			
			if (lastWriteFuture != null) {
				lastWriteFuture.awaitUninterruptibly();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (null != lastWriteFuture) {
			lastWriteFuture.awaitUninterruptibly();
		}
		channel.close().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
	}
}