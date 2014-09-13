package org.jboss.netty.codecs.json;

import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class EchoServerHandler extends SimpleChannelUpstreamHandler {
	private final AtomicLong transferredBytes = new AtomicLong();
	private static String END = new String(new char[] { 0 });

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		String msg = (String) e.getMessage();
		msg = msg.replace(END, "");
		JSONObject json;
		try {
			json = new JSONObject(msg);
			System.out.println("received:" + json.toString());
			e.getChannel().write(json.toString() + END)
					.addListener(new ChannelFutureListener() {
						@Override
						public void operationComplete(ChannelFuture future)
								throws Exception {
							future.getChannel().close();
						}
					});
			;
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}
