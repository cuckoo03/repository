package org.jboss.netty.example.qotm;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.FixedReceiveBufferSizePredictor;
import org.jboss.netty.channel.socket.DatagramChannel;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.example.BootstrapOptions;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class QuoteOfTheMomentClient {
	/**
	 * 브로드캐스트 통신 예제
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		DatagramChannelFactory factory = new NioDatagramChannelFactory(
				Executors.newCachedThreadPool());

		ConnectionlessBootstrap bootstrap = new ConnectionlessBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {

				return Channels.pipeline(
						new StringEncoder(),
						new StringDecoder(),
						new QuoteOfTheMomentClientHandler());
			}
		});

		bootstrap.setOption(BootstrapOptions.BROAD_CAST, "true");

		bootstrap.setOption(
				BootstrapOptions.RECEIVE_BUFFER_SIZE_PREDICTOR_FACTORY,
				new FixedReceiveBufferSizePredictor(1024));

		DatagramChannel channel = (DatagramChannel) bootstrap
				.bind(new InetSocketAddress(0));

		channel.write("QOTM?", new InetSocketAddress("255.255.255.255", 8080));

		if (!channel.getCloseFuture().await(1000)) {
			System.err.println("QOTM request timed out");
			channel.close().awaitUninterruptibly();
		}

		factory.releaseExternalResources();
	}
}