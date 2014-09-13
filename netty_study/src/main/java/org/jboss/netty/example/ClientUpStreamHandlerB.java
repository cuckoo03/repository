package org.jboss.netty.example;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ClientUpStreamHandlerB extends SimpleChannelHandler {
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) 
	throws Exception {
		System.out.println("Client B upstream:" + e.toString());
		
		super.handleUpstream(ctx, e);
	}
	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		System.out.println("Client B downstream:" + e.toString());

		super.handleDownstream(ctx, e);
	}
}
