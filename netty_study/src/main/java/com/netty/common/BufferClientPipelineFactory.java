package com.netty.common;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.util.Timer;

public class BufferClientPipelineFactory implements ChannelPipelineFactory {
	private final int READ_TIMEOUT = 5;
	private final int WRITE_TIMEOUT = 3;
	private List<DataChangeEventListener> list;
	private Timer timer;
	private ClientBootstrap bootstrap;

	public BufferClientPipelineFactory(List<DataChangeEventListener> list,
			Timer timer, ClientBootstrap bootstrap) {
		this.list = list;
		this.timer = timer;
		this.bootstrap = bootstrap;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelHandler idleStateHandler = new IdleStateHandler(timer,
				READ_TIMEOUT, WRITE_TIMEOUT, 0);

		ChannelPipeline p = Channels.pipeline();
		p.addLast("idle", idleStateHandler);
		p.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
		p.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
		p.addLast("handler", new BufferClientHandler(timer, bootstrap, list));
		return p;
	}
}
