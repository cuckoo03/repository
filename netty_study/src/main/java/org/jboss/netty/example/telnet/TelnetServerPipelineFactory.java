package org.jboss.netty.example.telnet;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

public class TelnetServerPipelineFactory implements ChannelPipelineFactory {
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192,
				Delimiters.lineDelimiter())); // up
		pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));//up
		pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));// down
//		pipeline.addLast("encoder", new CustomStringEncoder());// down
		pipeline.addLast("handler", new TelnetServerHandler());// up 
		
		return pipeline;
	}
}
