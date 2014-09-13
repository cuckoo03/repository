package org.jboss.netty.example.factorial;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class FactorialClient {
	public static void main(String[] args) {
		NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);

		final int count = 5;
		bootstrap.setPipelineFactory(new FactorialClientPipelineFactory(count));

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"127.0.0.1", 8080));

		Channel channel = future.awaitUninterruptibly().getChannel();

		FactorialClientHandler handler = (FactorialClientHandler) channel
				.getPipeline().getLast();

		System.out.format("Factorial of %,d is: %,d", count,
				handler.getFactorial());
		System.out.println("");
		
		channel.close();
		
		bootstrap.releaseExternalResources();
	}
}