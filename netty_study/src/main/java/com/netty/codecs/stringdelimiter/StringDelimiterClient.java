package com.netty.codecs.stringdelimiter;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class StringDelimiterClient {
	public static ChannelBuffer[] decoderChannelBuffer = new ChannelBuffer[] { ChannelBuffers
			.wrappedBuffer(new byte[] { '\n' }) };

	/**
	 * 구분자로 구분된 문자열이
	 * 한번에 여러 line으로 전송되는 데이터 처리
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ServerBootstrap sb = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		sb.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new DelimiterBasedFrameDecoder(8192,
								new ChannelBuffer[] { ChannelBuffers
										.wrappedBuffer(new byte[] { '\n' }) }),
						new StringEncoder(), new StringDecoder(),
						new StringDelimiterHandler());
			}
		});
		sb.bind(new InetSocketAddress(8080));

		ClientBootstrap cb = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		cb.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new DelimiterBasedFrameDecoder(8192,
								new ChannelBuffer[] { ChannelBuffers
										.wrappedBuffer(new byte[] { '\n' }) }),
						new StringEncoder(), new StringDecoder());
			}
		});

		ChannelFuture future = cb.connect(new InetSocketAddress("127.0.0.1",
				8080));
		future.awaitUninterruptibly();

		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			cb.releaseExternalResources();
			sb.releaseExternalResources();
			return;
		}

		future.getChannel().write("Success\t\n15\nabcde fghij \nklm ");

		future.getChannel().getCloseFuture().awaitUninterruptibly();

		cb.releaseExternalResources();
		sb.releaseExternalResources();
	}
}
