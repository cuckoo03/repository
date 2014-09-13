package com.netty.elevator.frame;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 * Composite Pattern (Component Class)
 * 
 * @author u2waremanager@gmail.com
 */
public abstract class Frame<V> {

	private String name;
	private int length;
	private V value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	// /////////////////////////////////////////
	//
	// /////////////////////////////////////////
	/**
	 * 주어진 Frame 객체를 자식 요소에 추가 한다.
	 * 
	 * @param child
	 *            자식 Frame 인스턴스
	 */
	public void addFrame(Frame child) {
		throw new RuntimeException("not implements");
	}

	/**
	 * 주어진 Frame 객체를 자식 요소에서 제거 한다.
	 * 
	 * @param child
	 */
	public void removeFrame(Frame child) {
		throw new RuntimeException("not implements");
	}

	/**
	 * 주어진 이름에 해당하는 Frame 객체를 자식 요소에서 찾아서 리턴한다.
	 * 
	 * @param name
	 */
	public Frame getFrame(String name) {
		throw new RuntimeException("not implements");
	}

	// /////////////////////////////////////////
	//
	// /////////////////////////////////////////
	/**
	 * 주어진 value 값에 해당 하는 message 를 추가한다.
	 * 
	 * @param value
	 * @param message
	 */
	public void addMessage(V value, Object message) {
		throw new RuntimeException("not implements");
	}

	/**
	 * 주어진 value 값에 해당 하는 message 를 제거한다.
	 * 
	 * @param value
	 */
	public void removeMessage(V value) {
		throw new RuntimeException("not implements");
	}

	/**
	 * 설정된 {@link Frame#getValue()} 에 해당하는 message 를 리턴한다.
	 * 
	 * @return message
	 */
	public Object getMessage() {
		throw new RuntimeException("not implements");
	}

	/**
	 * message 에 해당하는 value 를 {@link Frame#setValue(Object)} 로 설정한다.
	 * 
	 * @param message
	 */
	public void setMessage(Object message) {
		throw new RuntimeException("not implements");
	}

	// /////////////////////////////////////////
	//
	// /////////////////////////////////////////

	/**
	 * index 부터 {@link ChannelBuffer} 를 read 하여 {@link Frame#setValue(Object)}
	 * 하고 read 바이트 길이 만큼 더해서 새로운 index 를 리턴한다.
	 * 
	 * @param buffer
	 * @param index
	 * @return newIndex
	 * @throws Exception
	 */
	public abstract int decode(ChannelBuffer buffer, int index)
			throws Exception;

	/**
	 * {@link Frame#getValue()} 값을 index 부터 {@link ChannelBuffer} 에 write 하고,
	 * write 바이트 길이 만큼 더해서 새로운 index 를 리턴한다.
	 * 
	 * @param buffer
	 * @param index
	 * @return newIndex
	 * @throws Exception
	 */
	public abstract int encode(ChannelBuffer buffer, int index)
			throws Exception;
}