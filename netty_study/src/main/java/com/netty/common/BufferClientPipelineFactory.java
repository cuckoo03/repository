package com.netty.common;

import java.util.List;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class BufferClientPipelineFactory implements ChannelPipelineFactory {
	private List<DataChangeEventListener> list;

	public BufferClientPipelineFactory(List<DataChangeEventListener> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline p = Channels.pipeline();
		p.addLast("handler", new BufferClientHandler(list));
		return p;
	}
}
