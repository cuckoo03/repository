package com.ch16.pool.selector;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Iterator;

import com.ch16.pool.selector.handler.RequestHandler;
import com.ch16.queue.Queue;

public class RequestSelectorPool extends SelectorPoolAdaptor {

	private Queue queue = null;

	public RequestSelectorPool(Queue queue) {
		this(queue, 2);
	}

	public RequestSelectorPool(Queue queue, int size) {
		super.size = size;
		this.queue = queue;
		init();
	}

	private void init() {
		for (int i = 0; i < size; i++) {
			pool.add(createHandler(i));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.daum.javacafe.pool.SelectorPoolAdaptor#createHandler(int)
	 */
	protected Thread createHandler(int index) {
		Selector selector = null;
		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread handler = new RequestHandler(queue, selector, index);

		return handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.daum.javacafe.pool.selector.SelectorPoolIF#startAll()
	 */
	public void startAll() {
		Iterator<Thread> iter = pool.iterator();
		while (iter.hasNext()) {
			Thread handler = iter.next();
			handler.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.daum.javacafe.pool.selector.SelectorPoolIF#stopAll()
	 */
	public void stopAll() {
		Iterator<Thread> iter = pool.iterator();
		while (iter.hasNext()) {
			Thread handler = iter.next();
			handler.interrupt();
			handler = null;
		}
	}

}
