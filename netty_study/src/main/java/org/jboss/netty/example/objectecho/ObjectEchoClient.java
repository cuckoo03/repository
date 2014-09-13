package org.jboss.netty.example.objectecho;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
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
		String host = "127.0.0.1";
		int port = 8080;
		final int firstMessageSize = 256;

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
								.weakCachingConcurrentResolver(null)),
						new ObjectEchoClientHandler(firstMessageSize));
			}
		});

		bootstrap.connect(new InetSocketAddress(host, port));
	}
}