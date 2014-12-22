package com.ch16.pool.thread.processor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.ch16.events.Job;
import com.ch16.events.NIOEvent;
import com.ch16.pool.PoolManager;
import com.ch16.pool.buffer.ByteBufferPoolIF;
import com.ch16.pool.selector.handler.RequestHandler;
import com.ch16.queue.ChattingRoom;
import com.ch16.queue.Queue;

public class ReadWriteProcessor extends Thread {
	private Queue queue = null;

	public ReadWriteProcessor(Queue queue) {
		this.queue = queue;
	}

	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Job job = queue.pop(NIOEvent.READ_EVENT);
				SelectionKey key = (SelectionKey) job.getSession().get(
						RequestHandler.SELECTION_KEY);
				SocketChannel sc = (SocketChannel) key.channel();
				try {
					broadcast(sc);
				} catch (IOException e) {
					closeChannel(sc);
				}
			}
		} catch (Exception e) {
			// 주석해제시 index error 발생
			// e.printStackTrace();
		}
	}

	private void broadcast(SocketChannel sc) throws IOException {
		ByteBufferPoolIF bufferPool = (ByteBufferPoolIF) PoolManager
				.getInstance().getByteBufferPool();
		ByteBuffer buffer = null;
		try {
			buffer = bufferPool.getMemoryBuffer();

			for (int i = 0; i < 1; i++) {
				sc.read(buffer);
			}
			buffer.flip();

			Iterator<SocketChannel> iter = ChattingRoom.getInstance()
					.iterator();
			while (iter.hasNext()) {
				SocketChannel member = iter.next();
				if ((null != member) && member.isConnected()) {
					while (buffer.hasRemaining()) {
						member.write(buffer);
					}
					buffer.rewind();
				}
			}
		} finally {
			bufferPool.putBuffer(buffer);
		}
	}

	private void closeChannel(SocketChannel sc) {
		try {
			sc.close();
			ChattingRoom.getInstance().remove(sc);
		} catch (IOException e) {

		}
	}
}
