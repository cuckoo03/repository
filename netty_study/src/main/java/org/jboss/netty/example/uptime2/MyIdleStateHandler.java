package org.jboss.netty.example.uptime2;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class MyIdleStateHandler extends IdleStateAwareChannelHandler {
	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) {
		if (e.getState() == IdleState.READER_IDLE) {
			System.out.println("Reader idle, closing channel");
			e.getChannel().close();
			// e.getChannel().write("heartbeat-reader_idle");
		} else if (e.getState() == IdleState.WRITER_IDLE) {
			System.out.println("Writer idle, sending heartbeat");
			// e.getChannel().write("heartbeat-writer_idle");
		} else if (e.getState() == IdleState.ALL_IDLE) {
			System.out.println("All idle, sending heartbeat");
			// e.getChannel().write("heartbeat-all_idle");
		}
	}
}