package org.jboss.netty.example.objectecho;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

public class ObjectEchoClient {
	/**
	 * 현재 요청하는 서비스가 무엇인지 판단하여 파이프라인에 현재 핸드러를 삭제하고 
	 * 새로운 핸들러들을 등록
	 * @param args
	 */
	public static void main(String[] args) {
		args = new String[]{"127.0.01", "9001", "8"};
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		final int firstMessageSize = Integer.parseInt(args[2]);

		if (args.length < 2 || args.length > 3) {
			System.err.println("Usage: "
					+ ObjectEchoClient.class.getSimpleName()
					+ " <host> <port> <[<first mesage size>]");
		} else {
			host = args[0];
			port = Integer.parseInt(args[1]);
		}

		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		ClientBootstrap bootstrap = new ClientBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			// ObjectDecoder()는 3.2이상에서는 deprecated 되었다
			// ClassResolvers 사용해야함
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new ObjectEncoder(),
						new ObjectDecoder(ClassResolvers
								.cacheDisabled(getClass().getClassLoader())),
						new ObjectEchoClientHandler(firstMessageSize));
			}
		});

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));
		future.awaitUninterruptibly();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}
		System.err.println("client connected");
		
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
	}
}