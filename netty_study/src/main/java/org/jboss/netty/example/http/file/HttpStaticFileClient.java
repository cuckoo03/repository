package org.jboss.netty.example.http.file;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class HttpStaticFileClient {
	/**
	 * asynchronous large file streaming in HTTP
	 * 지정한 서버의 파일을 다운로드하는 예제
	 * 개선해야할 점
	 * 1.파일서버에 파일이 존재하지 않아도 클라이언트에 빈 파일이 생김
	 * 2.업로드 가능한 파일용량이 100M 까지는 가능하나 속도와 시간이 오래걸림.
	 * @param args
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws URISyntaxException {
		URI uri = new URI("http://127.0.0.1:8080/target/file100.txt");
//		URI uri = new URI("http://127.0.0.1:8080/");
		String host = uri.getHost();
		int port = uri.getPort();

		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("codec", new HttpClientCodec());
//				pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
				pipeline.addLast("handler", new HttpStaticFileClientHandler());
				return pipeline;
			}
		});

		ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(
				host, port));

		channelFuture.awaitUninterruptibly();
		if (!channelFuture.isSuccess()) {
			channelFuture.getChannel().close();
			bootstrap.releaseExternalResources();
			return;
		}

		HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
				HttpMethod.GET, host + "/file100.txt");
		request.setHeader(HttpHeaders.Names.HOST, host);
		request.setHeader(HttpHeaders.Names.CONNECTION,
				HttpHeaders.Values.CLOSE);

		channelFuture.getChannel().write(request);

		channelFuture.getChannel().getCloseFuture().awaitUninterruptibly();

		bootstrap.releaseExternalResources();
	}
}