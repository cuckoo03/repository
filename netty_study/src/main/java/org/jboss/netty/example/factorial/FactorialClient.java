package org.jboss.netty.example.factorial;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class FactorialClient {
	public static void main(String[] args) {
		NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);

		final int count = 10;
		bootstrap.setPipelineFactory(new FactorialClientPipelineFactory(count));

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"127.0.0.1", 9001));
		System.err.println("client connect");

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
class FactorialClientPipelineFactory implements ChannelPipelineFactory {
	private final int count;

	public FactorialClientPipelineFactory(int count) {
		this.count = count;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
//		pipeline.addLast("deflater", new ZlibEncoder(ZlibWrapper.GZIP));
//		pipeline.addLast("inflater", new ZlibDecoder(ZlibWrapper.GZIP));

		pipeline.addLast("decoder", new BigIntegerDecoder());
		pipeline.addLast("encoder", new NumberEncoder());

		pipeline.addLast("handler", new FactorialClientHandler(count));
		return pipeline;
	}
}