package org.jboss.netty.example.telnet;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

public class TelnetClientPipelineFactory implements ChannelPipelineFactory {
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

		// 받은 데이터를 하나이상의 구분자로 잘라주는 디코더
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(4096,
				Delimiters.lineDelimiter())); // up
		pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8)); //down
		pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8)); //up
		// 또는  StringEncoder를 직접 구현해도 된다
//		pipeline.addLast("encoder", new CustomStringEncoder()); //up
		pipeline.addLast("handler", new TelnetClientHandler()); // up
		
		return pipeline;
	}
}
