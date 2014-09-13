package com.netty.codecs.replayingdecoder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

import com.netty.codecs.replayingdecoder.MyReplayingDecoder.DecodingState;

public class MyReplayingDecoder extends ReplayingDecoder<DecodingState> {
	private Envelope message;

	public MyReplayingDecoder() {
		reset();
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer, DecodingState state) throws Exception {
		switch (state) {
		case VERSION:
			this.message.setVersion(Version.fromByte(buffer.readByte()));
			checkpoint(DecodingState.TYPE);
		case TYPE:
			this.message.setType(Type.fromByte(buffer.readByte()));
			checkpoint(DecodingState.PAYLOAD_LENGTH);
		case PAYLOAD_LENGTH:
			int size = buffer.readInt();
			if (size <= 0) {
				throw new Exception("invalid content size");
			}

			byte[] content = new byte[size];
			this.message.setPayload(content);
			checkpoint(DecodingState.PAYLOAD);
		case PAYLOAD:
			buffer.readBytes(this.message.getPayload(), 0,
					this.message.getPayload().length);

			try {
				return this.message;
			} finally {
				reset();
			}

		default:
			throw new Exception("unknown decoding state:" + state);
		}
	}

	private void reset() {
		checkpoint(DecodingState.VERSION);
		this.message = new Envelope();
	}

	public enum DecodingState {
		VERSION, TYPE, PAYLOAD_LENGTH, PAYLOAD,
	}
	public enum DecodingState2 {
		VERSION2, TYPE2, PAYLOAD_LENGTH2, PAYLOAD2,
	}

}
