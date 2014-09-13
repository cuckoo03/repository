package org.jboss.netty.example.localtime;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.example.localtime.LocalTimeClientPipelineFactory;

public class LocalTimeClient {
	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("arg length 3");
			return;
		}

		String host = "127.0.0.1";
		int port = 8080;
		Collection<String> cities = parseCities(args, 2);
		if (null == cities) {
			return;
		}

		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new LocalTimeClientPipelineFactory());

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));

		Channel channel = future.awaitUninterruptibly().getChannel();

		LocalTimeClientHandler handler = channel.getPipeline().get(
				LocalTimeClientHandler.class);

		List<String> response = handler.getLocalTimes(cities);

		channel.close().awaitUninterruptibly();

		bootstrap.releaseExternalResources();

		Iterator<String> i1 = cities.iterator();
//		Iterator<String> i2 = response.iterator();
		while (i1.hasNext()) {
//			System.out.format("%28s: %s%n", i1.next(), i2.next());
		}
	}

	private static Collection<String> parseCities(String[] args, int offset) {
		List<String> cities = new ArrayList<String>();
		for (int i = offset; i < args.length; i++) {
			if (!args[i].matches("^[_A-Za-z]+/[_A-Za-z]+$")) {
				System.err.println("Syntax error: '" + args[i] + "'");
				return null;
			}
			cities.add(args[i].trim());
		}
		return cities;
	}

}
