package com.nettyinaction.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.nio.charset.Charset;

public class OioServer {
	public void server(int port) throws InterruptedException {
		final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
				"hi하이", Charset.forName("UTF-8")));
		EventLoopGroup group = new OioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group).channel(OioServerSocketChannel.class)
					.localAddress(port)
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(
									new ChannelInboundHandlerAdapter() {
										@Override
										public void channelActive(
												ChannelHandlerContext ctx) {
											System.err.println("channelActive");
											ctx.write(buf.duplicate())
													.addListener(
															ChannelFutureListener.CLOSE);
											ctx.flush();
										}
									});
						}
					});
			ChannelFuture f = b.bind().sync();
			System.err.println("Server bind");
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	public static void main(String[] args) throws Exception {
		args = new String[] { "9001" };
		int port = Integer.parseInt(args[0]);
		new OioServer().server(port);
	}
}
