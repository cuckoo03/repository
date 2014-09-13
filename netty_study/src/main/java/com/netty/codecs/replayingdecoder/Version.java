package com.netty.codecs.replayingdecoder;

public enum Version {
	VERSION1((byte) 0x01), VERSION2((byte) 0x02), UNKNOWN((byte) 0x10);

	private final byte v;

	private Version(byte v) {
		this.v = v;
	}

	public static Version fromByte(byte b) {
		for (Version version : values()) {
			if (b == version.v) {
				return version;
			}
		}
		return UNKNOWN;
	}

	public byte getByteValue() {
		return v;
	}
}
