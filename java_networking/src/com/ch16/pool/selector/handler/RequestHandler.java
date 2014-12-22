package com.ch16.pool.selector.handler;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.ch16.events.Job;
import com.ch16.events.NIOEvent;
import com.ch16.pool.selector.handler.HandlerAdaptor;
import com.ch16.queue.ChattingRoom;
import com.ch16.queue.Queue;

public class RequestHandler extends HandlerAdaptor {
	public static final String SELECTION_KEY = "SelectionKey";

	private Queue queue = null;
	private Selector selector = null;
	private String name = "RequestHandler";

	private Vector<SocketChannel> newClients = new Vector<SocketChannel>();

	public RequestHandler(Queue queue, Selector selector, int index) {
		this.queue = queue;
		this.selector = selector;
		setName(name + index);
	}

	@Override
	public void addClient(SocketChannel sc) {
		newClients.add(sc);
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				processNewConnection();

				int keysReady = selector.select(1000);
				System.out.println("@RequestHandler(" + getName()
						+ ") selected:" + keysReady);

				if (keysReady > 0) {
					processRequest();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processRequest() {
		Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
		while (iter.hasNext()) {
			SelectionKey key = iter.next();
			iter.remove();
			pushMyJob(key);
		}
	}

	private void pushMyJob(SelectionKey key) {
		Map<String, Object> session = new HashMap<String, Object>();
		session.put(SELECTION_KEY, key);
		Job job = new Job(NIOEvent.READ_EVENT, session);

		queue.push(job);
	}

	private synchronized void processNewConnection()
			throws ClosedChannelException {
		Iterator<SocketChannel> iter = newClients.iterator();
		while (iter.hasNext()) {
			SocketChannel sc = iter.next();
			sc.register(selector, SelectionKey.OP_READ);
			ChattingRoom.getInstance().add(sc);
			System.out.println("@RequestHandler(" + getName()
					+ ") success regist:" + sc.toString());
		}
		newClients.clear();
	}
}
