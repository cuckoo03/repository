package com.netty.elevator;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

public class ElevatorFrameDecoder extends OneToOneDecoder {
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		ChannelBuffer buffer = (ChannelBuffer) msg;
		byte b_id = buffer.getByte(3);
		byte b_state = buffer.getByte(8);
		Elevator elevator = new Elevator();
		elevator.setId(b_id);
		elevator.setState(getState(b_state));
		return elevator;
	}

	private String getState(byte state) {
		switch (state) {
		case 0x01:
			return "통신이상";
		case 0x02:
			return "자동운전";
		case 0x06:
			return "승강기고장";
		default:
			return null;
		}
	}

}
