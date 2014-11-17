package com.netty.common;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

public class BufferServerPipelineFactory implements ChannelPipelineFactory {
	private Map<String, Channel> channelMap = new HashMap<>();

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline p = Channels.pipeline();
		p.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
		p.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
		p.addLast("handler", new BufferServerHandler(channelMap));
		return p;
	}
}
