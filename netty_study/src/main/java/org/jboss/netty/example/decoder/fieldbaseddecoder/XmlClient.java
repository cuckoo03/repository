package org.jboss.netty.example.decoder.fieldbaseddecoder;

import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.local.DefaultLocalClientChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class XmlClient {

	/**
	 * 소스 미완성
	 * @param args
	 */
	public static void main(String[] args) {
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline p = Channels.pipeline();
				p.addLast("encoder", new StringEncoder());
				p.addLast("decoder", new StringDecoder());
				return p;
			}
		});

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"127.0.0.1", 8000));
		future.awaitUninterruptibly();

		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}

		org.jboss.netty.channel.Channel channel = future.getChannel();
		future = channel.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><context><data id=\"한글\" pwd=\"seban00\"/></context>");
		future.awaitUninterruptibly();
		channel.getCloseFuture().awaitUninterruptibly();

		bootstrap.releaseExternalResources();
	}
}