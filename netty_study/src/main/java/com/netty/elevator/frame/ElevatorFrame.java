package com.netty.elevator.frame;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class ElevatorFrame extends FrameDecoder {
	private String name;
	private List<ElevatorFrameItem> childs = new ArrayList<ElevatorFrameItem>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addFrame(ElevatorFrameItem child) {
		childs.add(child);
	}

	public ElevatorFrameItem getFrame(String name) {
		return null;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		return 10;
	}
}