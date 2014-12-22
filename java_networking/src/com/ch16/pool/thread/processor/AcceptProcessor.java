package com.ch16.pool.thread.processor;

import java.nio.channels.SocketChannel;

import com.ch16.events.Job;
import com.ch16.events.NIOEvent;
import com.ch16.pool.PoolManager;
import com.ch16.pool.selector.handler.AcceptHandler;
import com.ch16.pool.selector.handler.HandlerAdaptor;
import com.ch16.queue.Queue;

public class AcceptProcessor extends Thread {
	private Queue queue = null;

	public AcceptProcessor(Queue queue) {
		this.queue = queue;
	}

	public void run() {
		try {
			Job job = queue.pop(NIOEvent.ACCEPT_EVENT);
			SocketChannel sc = (SocketChannel) job.getSession().get(
					AcceptHandler.SOCKET_CHANNEL);
			sc.configureBlocking(false);

			HandlerAdaptor handler = (HandlerAdaptor) PoolManager.getInstance()
					.getRequestSelectorPool().get();

			handler.addClient(sc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
