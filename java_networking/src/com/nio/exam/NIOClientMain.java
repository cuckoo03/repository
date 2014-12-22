package com.nio.exam;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.BufferUtil;

public class NIOClientMain {
	private Selector selector;
	private SocketChannel socketChannel;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NIOClientMain nioClient = new NIOClientMain();
		nioClient.init();
		nioClient.start();
	}

	private void init() {
		try {
			System.out.println("client init");
			selector = Selector.open();

			InetSocketAddress inetAddress = new InetSocketAddress("127.0.0.1",
					10001);
			socketChannel = SocketChannel.open(inetAddress);

			socketChannel.configureBlocking(false);

			System.out.println("isconnected:" + socketChannel.isConnected());
			System.out.println("isconnectionPending:"
					+ socketChannel.isConnectionPending());

			socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void start() {
		System.out.println("client start");
		new WritableThread(socketChannel).start();
		new ReadableThread(selector).start();
	}
}

class WritableThread extends Thread {
	private SocketChannel socketChannel;
	private InputStreamReader isr = new InputStreamReader(System.in);
	private ByteBuffer buf = ByteBuffer.allocateDirect(1024);

	public WritableThread(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public void run() {
		while (!Thread.currentThread().interrupted()) {
			try {
				BufferedReader br = new BufferedReader(isr);
				String message = br.readLine();
				System.out.println("client input messasge:" + message);
				buf.put(message.getBytes());
				buf.flip();
				try {
					socketChannel.write(buf);
				} catch (ClosedChannelException e) {
					System.out.println("서버가 종료되었습니다 쓰기를 종료합니다");
					Thread.currentThread().interrupt();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				buf.clear();
			}
		}
	}
}

class ReadableThread extends Thread {
	private Selector selector;

	public ReadableThread(Selector selector) {
		this.selector = selector;
	}

	public void run() {
		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
		while (!Thread.currentThread().interrupted()) {
			try {
				int n = selector.select();
				if (0 == n) {
					return;
				}
				Iterator<SelectionKey> iter = selector.selectedKeys()
						.iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					if (key.isReadable()) {
						SocketChannel socketChannel = (SocketChannel) key
								.channel();

						try {
							socketChannel.read(buf);
						} catch (IOException e) { // server가 종료되었을 때 발생
							socketChannel.close();
							System.out.println("서버가 종료되었습니다 읽기를 종료합니다");
							Thread.currentThread().interrupt();
							e.printStackTrace();
						}

						buf.flip();
						System.out.println("client receive message:"
								+ BufferUtil.byteBufferToString(buf));
						buf.clear();
					}
					iter.remove();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}