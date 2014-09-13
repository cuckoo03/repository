package org.jboss.netty.example.local;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;

public class LocalServerPipelineFactory implements ChannelPipelineFactory {
	private final ExecutionHandler executionHandler;

	public LocalServerPipelineFactory(Executor eventExecutor) {
		this.executionHandler = new ExecutionHandler(eventExecutor);
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

		pipeline.addLast("encoder", new StringEncoder());
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("executor", executionHandler);
		pipeline.addLast("handler", new EchoCloseServerHandler());
		return pipeline;
	}
}

class EchoCloseServerHandler implements ChannelUpstreamHandler,
		ChannelDownstreamHandler {
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		// client가 write시 MessagEvent가 생성된다
		if (e instanceof MessageEvent) {
			final MessageEvent evt = (MessageEvent) e;
			String msg = (String) evt.getMessage();
			if (msg.equalsIgnoreCase("quit")) {
				Channels.close(e.getChannel());
				return;
			}
		}
		ctx.sendUpstream(e);
	}

	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof MessageEvent) {
			final MessageEvent evt = (MessageEvent) e;
			String msg = (String) evt.getMessage();
			if (msg.equalsIgnoreCase("quit")) {
				Channels.close(e.getChannel());
				return;
			}

			System.err.println("");
			System.err.println("Server:" + msg);

			Channels.write(e.getChannel(), msg);
		}
		ctx.sendDownstream(e);
	}
}