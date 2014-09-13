package org.jboss.netty.example.qotm;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.FixedReceiveBufferSizePredictorFactory;
import org.jboss.netty.channel.socket.DatagramChannel;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.example.BootstrapOptions;
import org.jboss.netty.example.telnet.CustomStringEncoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

public class QuoteOfTheMomentServer {
	public static void main(String[] args) {
		DatagramChannelFactory factory = new NioDatagramChannelFactory(
				Executors.newCachedThreadPool());

		ConnectionlessBootstrap bootstrap = new ConnectionlessBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new StringEncoder(),
						new StringDecoder(),
						new QuoteOfTheMomentServerHandler());
			}
		});

		bootstrap.setOption(
				BootstrapOptions.RECEIVE_BUFFER_SIZE_PREDICTOR_FACTORY,
				new FixedReceiveBufferSizePredictorFactory(1024));

		bootstrap.bind(new InetSocketAddress(8080));
	}
}