package org.jboss.netty.example.factorial;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class FactorialServer {
	public static void main(String[] args) {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new FactorialServerPipelineFactory());

		bootstrap.bind(new InetSocketAddress(9001));
		System.err.println("server bind");
	}
}
class FactorialServerPipelineFactory implements ChannelPipelineFactory{

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		
//		pipeline.addLast("deflater", new ZlibEncoder(ZlibWrapper.GZIP));
//		pipeline.addLast("inflater", new ZlibDecoder(ZlibWrapper.GZIP));
		
		pipeline.addLast("decoder", new BigIntegerDecoder());
		pipeline.addLast("encoder", new NumberEncoder());
		
		pipeline.addLast("handler", new FactorialServerHandler());
		
		return pipeline;
	}

}