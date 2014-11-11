package org.jboss.netty.example.http.file;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.codec.http.HttpServerCodec;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

public class HttpStaticFileServer {
	public static void main(String[] args) {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				// 서버쪽은 HttpServerCodec을 쓰거나 encoder, decoder를 쓰면 된다 
				// 클라이언트측은 HttpResponseEncoder, HttpRequestDecoder를 쓰면
				// 런타임 오류 발생
				pipeline.addLast("codec", new HttpServerCodec());
//				pipeline.addLast("encoder", new HttpResponseEncoder());
//				pipeline.addLast("decoder", new HttpRequestDecoder());
				pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
				pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

				pipeline.addLast("handler", new HttpStaticFileServerHandler());
				return pipeline;
			}
		});

		bootstrap.bind(new InetSocketAddress(9001));
		System.err.println("server bind");
	}
}
