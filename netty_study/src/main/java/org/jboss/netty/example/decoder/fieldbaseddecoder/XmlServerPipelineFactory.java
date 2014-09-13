package org.jboss.netty.example.decoder.fieldbaseddecoder;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class XmlServerPipelineFactory implements ChannelPipelineFactory {
	private static final int MAX_FRAME_LENGTH = 1024 * 30; // 최대 패킷 사이즈
	private static final int LENGTH_FIELD_OFFSET = 0; // 패킷 길이 표시 시작
	private static final int LENGTH_FIELD_LENGTH = 2; // 패킷 길이 사이즈
	private static final int LENGTH_ADJUSTMENT = 0; // 버릴패킷 위치 시작
	private static final int INITIA_BYTES_TO_STRIP = 2; // 버릴패킷 위치 종료

	public XmlServerPipelineFactory() {
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		// 패킷을보낼시 앞에 2바이트 문자길이를 붙임
		pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(
				MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH,
				LENGTH_ADJUSTMENT, INITIA_BYTES_TO_STRIP));
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());
		pipeline.addLast("handler", new XmlHandler());
		return pipeline;
	}
}