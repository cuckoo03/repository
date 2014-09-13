package org.jboss.netty.example.decoder.fieldbaseddecoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class XmlServer {
	public static void main(String[] args) throws Exception {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new XmlServerPipelineFactory());
		bootstrap.bind(new InetSocketAddress("127.0.0.1", 8000));
		System.out.println("success!!");
	}
}