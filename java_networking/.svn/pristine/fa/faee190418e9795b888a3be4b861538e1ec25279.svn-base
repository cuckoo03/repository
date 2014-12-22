package com.nio.exam;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NIOServerMain {
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private List<SocketChannel> socketChannelQueue = new LinkedList<SocketChannel>();

	/**
	 * NIO Server Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		NIOServerMain nioServer = new NIOServerMain();
		nioServer.init();
		nioServer.start();
	}

	private void init() {
		try {
			selector = Selector.open();

			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			ServerSocket serverSocket = serverSocketChannel.socket();
			SocketAddress sa = new InetSocketAddress("127.0.0.1", 10001);
			serverSocket.bind(sa);

			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void start() {
		while (true) {
			try {
				System.out.println("server: waiting");
				int n = selector.select();
				if (0 == n) {
					continue;
				}
				Iterator<SelectionKey> iter = selector.selectedKeys()
						.iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					iter.remove();
					if (key.isAcceptable()) {
						accept(key);
					}

					if (key.isReadable()) {
						read(key);
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	private void accept(SelectionKey key) {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();
		try {
			SocketChannel socketChannel = serverSocketChannel.accept();
			Socket socket = socketChannel.socket();

			if (null == socket) {
				System.out.println("socket is null");
				return;
			}

			System.out.println("server:" + socket.getLocalAddress() + ":"
					+ socket.getLocalPort() + " connected");

			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);

			socketChannelQueue.add(socketChannel);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
		}
	}

	private void read(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
		try {
			socketChannel.read(buf);
		} catch (IOException e) { // client disconnect exception
			try {
				socketChannel.close();
			} catch (Exception e1) {
				System.out.println(e);
			}
			System.out.println("client disconnect");

			socketChannelQueue.remove(socketChannel);
		}

		try {
			buf.flip();

			byte[] data = new byte[buf.limit()];
			buf.get(data, 0, buf.limit());
			System.out.println("server: receive message=" + new String(data));

			buf.rewind();
			for (SocketChannel item : socketChannelQueue) {
				if (null != item) {
					item.write(buf);
					buf.rewind();
					System.out.println("broadcast message to " + item.socket()
							+ new String(data));
					System.out.println(item.socket());
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
