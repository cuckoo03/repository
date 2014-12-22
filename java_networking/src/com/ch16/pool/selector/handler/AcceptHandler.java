package com.ch16.pool.selector.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ch16.events.Job;
import com.ch16.events.NIOEvent;
import com.ch16.queue.Queue;

public class AcceptHandler extends Thread {
	public static final String SOCKET_CHANNEL = "SocketChannel";

	private Queue queue = null;
	private Selector selector = null;
	private int port = 9090;
	private String name = "AcceptHandler-";

	public AcceptHandler(Queue queue, Selector selector, int port, int index) {
		this.queue = queue;
		this.selector = selector;
		this.port = port;
		setName(name + index);
		init();
	}

	private void init() {
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);

			InetSocketAddress address = new InetSocketAddress("127.0.0.1", port);
			ssc.socket().bind(address);

			System.out.println("@AccptHandler(" + getName() + ") Bound to "
					+ address);

			ssc.register(this.selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				selector.select();
				acceptPendingConnections();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void acceptPendingConnections() throws IOException {
		Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
		while (iter.hasNext()) {
			SelectionKey key = iter.next();
			iter.remove();

			ServerSocketChannel readyChannel = (ServerSocketChannel) key
					.channel();
			SocketChannel sc = readyChannel.accept();

			System.out.println("@Accepthadnelr(" + getName()
					+ ") connection accpeted from"
					+ sc.socket().getInetAddress());

			pushMyJob(sc);
		}
	}

	private void pushMyJob(SocketChannel sc) {
		Map<String, Object> session = new HashMap<String, Object>();
		session.put(SOCKET_CHANNEL, sc);
		Job job = new Job(NIOEvent.ACCEPT_EVENT, session);
		queue.push(job);
	}
}
