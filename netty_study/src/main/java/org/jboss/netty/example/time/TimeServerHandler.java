package org.jboss.netty.example.time;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

@ChannelPipelineCoverage("one")
public class TimeServerHandler extends SimpleChannelHandler {
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		Channel ch = e.getChannel();

//		ChannelBuffer time = ChannelBuffers.buffer(8);
//		time.writeLong((System.currentTimeMillis() / 1000));
		UnixTime time = new UnixTime(System.currentTimeMillis() / 1000);

		ChannelFuture f = ch.write(time);

		f.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future)
					throws Exception {
				Channel ch = future.getChannel();
				ch.close();
				System.out.println("TimeServerHandler Channel close");
			}
		});
		
		// 또는 아래와 같은 리스너를 추가하면 커넥션 종료가 된다
//		f.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}