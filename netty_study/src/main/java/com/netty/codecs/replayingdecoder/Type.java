package com.netty.codecs.replayingdecoder;

import java.lang.Character.UnicodeScript;

public enum Type {
	TYPE1((byte) 0x01), TYPE2((byte) 0x02), UNKNOWN((byte) 0x10);

	private final byte t;

	private Type(byte t) {
		this.t = t;
	}

	public byte getType() {
		return t;
	}

	public byte getByteValue() {
		return t;
	}

	public static Type fromByte(byte t) {
		for (Type version : values()) {
			if (t == version.t) {
				return version;
			}
		}
		return UNKNOWN;
	}
}
