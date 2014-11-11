package org.jboss.netty.example.portunification2;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * 실시간으로 핸들러를 변경하는 예제
 * @author cuckoo03
 *
 */
public class HandlerChangeClient {
	public void start() {

	}

	public static void main(String[] args) {
		ClientBootstrap b = new ClientBootstrap(
				new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		b.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new ClientHandler());
			}
		});
		b.connect(new InetSocketAddress("127.0.0.1", 9001));
	}
}
class ClientHandler extends SimpleChannelUpstreamHandler {
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		ChannelPipeline p = ctx.getPipeline();
		p.addLast("handler2", new ClientHandler2());
		p.remove(this);
		
	}
}
class ClientHandler2 extends SimpleChannelUpstreamHandler {
	public ClientHandler2() {
		System.out.println("ClientHandler2");
	}
}