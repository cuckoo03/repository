package org.jboss.netty.example.decoder.onetoone;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.local.DefaultLocalClientChannelFactory;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.jboss.netty.channel.local.LocalAddress;
import org.jboss.netty.example.decoder.MyClassProtocol;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLogLevel;

public class OneToOneExample {
	public static void main(String[] args) {
		LocalAddress socketAddress = new LocalAddress("1");

		ServerBootstrap sb = new ServerBootstrap(
				new DefaultLocalServerChannelFactory());
		sb.getPipeline().addLast("encoder", new MyOneToOneEncoder());
		sb.getPipeline().addLast("decoder", new MyOneToOneDecoder());
		sb.getPipeline().addLast("handler", new ServerHandler());
		sb.getPipeline().addLast("log",
				new LoggingHandler(InternalLogLevel.INFO));

		sb.bind(socketAddress);

		ClientBootstrap cb = new ClientBootstrap(
				new DefaultLocalClientChannelFactory());

		cb.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new MyOneToOneEncoder(),
						new MyOneToOneDecoder(), new LoggingHandler(
								InternalLogLevel.INFO), new ClientHandler());
			}
		});

		ChannelFuture channelFuture = cb.connect(socketAddress);
		channelFuture.awaitUninterruptibly();
		channelFuture.getChannel().write(new MyClassProtocol(1, "1234567890헬로우1234567890"));
		
		channelFuture.getChannel().close();

		channelFuture.getChannel().getCloseFuture().awaitUninterruptibly();

		cb.releaseExternalResources();
		sb.releaseExternalResources();
	}
}