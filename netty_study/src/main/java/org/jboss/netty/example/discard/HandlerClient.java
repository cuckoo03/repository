package org.jboss.netty.example.discard;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * 등록한 다음 순서의 다른 업, 다운 스트림을 호출하기 위해서는 오버라이드한 메서드에서 슈퍼메서드를 호출해야 한다.
 * @author cuckoo03
 *
 */
public class HandlerClient {
	public static void main(String[] args) {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline p = Channels.pipeline();
				p.addLast("up1", new SimpleChannelUpstreamHandler() {
					@Override
					public void messageReceived(ChannelHandlerContext ctx,
							MessageEvent e) throws Exception {
						ChannelBuffer buf = (ChannelBuffer) e.getMessage();
						System.out.println("up1 messageReceived:" + buf.capacity());
						ctx.sendUpstream(e);
					}
				});
				p.addLast("up2", new SimpleChannelUpstreamHandler() {
					@Override
					public void messageReceived(ChannelHandlerContext ctx,
							MessageEvent e) throws Exception {
						ChannelBuffer buf = (ChannelBuffer) e.getMessage();
						System.out.println("up2 messageReceived:" + buf.capacity());
						Thread.sleep(1000);
						e.getChannel().write(e.getMessage());
					}
					@Override
					public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
						e.getCause().printStackTrace();
						e.getChannel().close();
					}
				});
				p.addLast("down2", new SimpleChannelDownstreamHandler() {
					@Override
					public void writeRequested(ChannelHandlerContext ctx,
							MessageEvent e) throws Exception {
						System.out.println("down2 writeRequested");
						ctx.sendDownstream(e);
					}
				});
				p.addLast("down1", new SimpleChannelDownstreamHandler() {
					@Override
					public void writeRequested(ChannelHandlerContext ctx,
							MessageEvent e) throws Exception {
						System.out.println("down1 writeRequested");
						ctx.sendDownstream(e);
					}
				});
				return p;
			}
		});

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"127.0.0.1", 9001));
		future.awaitUninterruptibly();
		if (!future.isSuccess()) {
			System.out.println("connect fail");
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		};
		System.err.println("Client connected");
		
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
		System.out.println("Client end");
	}
}