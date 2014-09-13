package org.jboss.netty.codecs.json;

import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class EchoClientHandler extends SimpleChannelUpstreamHandler {
	private final ChannelBuffer firstMessage;
	private final AtomicLong transferredBytes = new AtomicLong();
	private static String END = new String(new char[] { 0 });

	public EchoClientHandler(int firstMessageSize) {
		this.firstMessage = ChannelBuffers.buffer(firstMessageSize);
		for (int i = 0; i < firstMessage.capacity(); i++) {
			firstMessage.writeByte((byte) i);
		}
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		JSONObject json = new JSONObject();
		try {
			json.put("name", "1");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		System.out.println("client : channelConnected : " + json);
		e.getChannel().write(json.toString() + END);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		String msg = (String) e.getMessage();
		msg = msg.replaceAll(END, "");

		JSONObject json;
		try {
			json = new JSONObject(msg);
			System.out.println("received:" + json.toString());
			// e.getChannel().write(e.getMessage());
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