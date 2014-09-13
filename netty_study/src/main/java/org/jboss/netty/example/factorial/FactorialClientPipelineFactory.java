package org.jboss.netty.example.factorial;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;

public class FactorialClientPipelineFactory implements ChannelPipelineFactory {

	private final int count;

	public FactorialClientPipelineFactory(int count) {
		this.count = count;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

//		pipeline.addLast("deflater", new ZlibEncoder(ZlibWrapper.GZIP));
//		pipeline.addLast("inflater", new ZlibDecoder(ZlibWrapper.GZIP));

		pipeline.addLast("decoder", new BigIntegerDecoder());
		pipeline.addLast("encoder", new NumberEncoder());

		pipeline.addLast("handler", new FactorialClientHandler(count));
		return pipeline;
	}
}