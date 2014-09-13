package org.jboss.netty.example.telnet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;
import org.jboss.netty.channel.SimpleChannelHandler;

public class CustomStringEncoder extends SimpleChannelDownstreamHandler {
	/**
	 * string을 channel buffer로 변경해주는 클래스
	 * 기본 패키지의 StringEncoder 클래스를 사용해도 된다
	 */
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {
		String s= (String) e.getMessage();
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(s.getBytes());
		Channels.write(ctx, e.getFuture(), buf);
	}
}
