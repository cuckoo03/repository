package com.ch16.queue;

import java.nio.channels.SocketChannel;
import java.util.Vector;

public class ChattingRoom extends Vector<SocketChannel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ChattingRoom instance = new ChattingRoom();

	public static Vector<SocketChannel> getInstance() {
		if (null == instance) {
			synchronized (ChattingRoom.class) {
				instance = new ChattingRoom();
			}
		}
		return instance;
	}

	private ChattingRoom() {
	}
}
