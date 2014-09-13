package com.netty.elevator.frame;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 * @author u2waremanager@gmail.com
 */
public class NumberFrame extends FrameItem<Number> {

	private boolean isUnsigned;

	public NumberFrame(String propertyName, int length) {
		this(propertyName, length, false, null);
	}

	public NumberFrame(String propertyName, int length, boolean isUnsigned) {
		this(propertyName, length, isUnsigned, null);
	}

	public NumberFrame(String propertyName, int length, Number value) {
		this(propertyName, length, false, null);
	}

	public NumberFrame(String propertyName, int length, boolean isUnsigned,
			Number value) {
		if (length != 1 && length != 2 && length != 3 && length != 4
				&& length != 8) {
			throw new IllegalArgumentException(
					"length must be either 1, 2, 3, 4, or 8: " + length);
		}
		super.setName(propertyName);
		super.setLength(length);
		super.setValue(value);
		this.isUnsigned = isUnsigned;
	}

	public int decode(ChannelBuffer buffer, int index) throws Exception {

		int size = buffer.writerIndex();
		if (size < (index + getLength())) {
			throw new IndexOutOfBoundsException(getName());
		}

		Number newValue = null;
		if (getLength() == 1) {
			if (isUnsigned) {
				newValue = buffer.getUnsignedByte(index);
			} else {
				newValue = buffer.getByte(index);
			}
		} else if (getLength() == 2) {
			if (isUnsigned) {
				newValue = buffer.getUnsignedShort(index);
			} else {
				newValue = buffer.getShort(index);
			}
		} else if (getLength() == 3) {
			if (isUnsigned) {
				newValue = buffer.getUnsignedMedium(index);
			} else {
				newValue = buffer.getMedium(index);
			}
		} else if (getLength() == 4) {
			if (isUnsigned) {
				newValue = buffer.getUnsignedInt(index);
			} else {
				newValue = buffer.getInt(index);
			}
		} else if (getLength() == 8) {
			newValue = buffer.getLong(index);
		}

		super.setValue(newValue);
		// logger.debug("size="+size+",idx="+index+",length="+getLength()+",name="+getName()+",value="+getValue()+",message="+getMessage());
		return index + getLength();
	}

	public int encode(ChannelBuffer buffer, int index) throws Exception {
		throw new RuntimeException("not implements");
	}
}