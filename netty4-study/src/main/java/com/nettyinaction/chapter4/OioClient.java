package com.nettyinaction.chapter4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class OioClient {
	private final String host;
	private final int port;

	public OioClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.remoteAddress(new InetSocketAddress(host, port))
					.handler(new ChannelInitializer<Channel>() {
						@Override
						public void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(
									new ChannelInboundHandlerAdapter() {
										@Override
										public void channelActive(
												ChannelHandlerContext ctx) {
											System.err.println("channelActive");
										}

										@Override
										public void channelRead(
												ChannelHandlerContext ctx,
												Object msg) {
											System.out
													.println("Client received:"
															+ msg);
										}
									});
						}
					});

			ChannelFuture f = b.connect().sync();
			System.err.println("Client connect");
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	public static void main(String[] args) throws Exception {
		args = new String[] { "127.0.0.1", "9001" };
		if (args.length < 2) {
			System.err.println("Usage:EchoClient <host> <port>");
			System.exit(0);
		}
		final String host = args[0];
		int port = Integer.parseInt(args[1]);
		new OioClient(host, port).start();
	}
}
