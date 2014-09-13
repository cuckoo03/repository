package com.netty.string;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.local.DefaultLocalClientChannelFactory;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.jboss.netty.channel.local.LocalAddress;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class CharToStringClient {

	/**
	 * char 배열로 보내진 문자열 변환 방법
	 * http://blog.naver.com/PostView.nhn?blogId=seban21&logNo=70111340480&parentCategoryNo=&categoryNo=135&viewDate=&isShowPopularPosts=false&from=postView
	 * @param args
	 */
	public static void main(String[] args) {
		LocalAddress socketAddress = new LocalAddress(0);

		ServerBootstrap sb = new ServerBootstrap(
				new DefaultLocalServerChannelFactory());

		sb.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new CharToStringHandler());
			}
		});

		sb.bind(socketAddress);

		ClientBootstrap cb = new ClientBootstrap(
				new DefaultLocalClientChannelFactory());

		cb.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline();
			}
		});

		ChannelFuture future = cb.connect(socketAddress).awaitUninterruptibly();

		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			sb.releaseExternalResources();
			return;
		}

		ChannelBuffer buf = ChannelBuffers.buffer(30);
		buf.writeBytes("hello world|bye world ".getBytes());
		future.getChannel().write(buf);
		future.getChannel().close().awaitUninterruptibly();
		
		cb.releaseExternalResources();
		sb.releaseExternalResources();
	}
}