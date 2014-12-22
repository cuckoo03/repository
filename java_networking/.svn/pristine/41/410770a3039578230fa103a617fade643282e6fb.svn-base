package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.utils.SystemVar;

public class Exam15_2SimpleChatClient {
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 10001;

	private static FileHandler fileHandler;
	private static Logger logger = Logger.getLogger("net.daum.javacafe");

	private Selector selector = null;
	private SocketChannel sc = null;

	private Charset charset = null;
	private CharsetDecoder decoder = null;

	private Exam14_7ByteBufferPool bufferPool = null;

	public Exam15_2SimpleChatClient() {
		SystemVar.printEncodingTypes();
		charset = charset.forName(SystemVar.SYSTEM_IN_ENCODING);
		decoder = charset.newDecoder();
	}

	public void initServer() {
		try {
			selector = Selector.open();
			sc = SocketChannel.open(new InetSocketAddress(HOST, PORT));
			sc.configureBlocking(false);

			sc.register(selector, SelectionKey.OP_READ);

			bufferPool = new Exam14_7ByteBufferPool(2048, 2048, new File(
					"data.bin"));
		} catch (IOException e) {
			logger.log(Level.WARNING, "simpleChatServer.initServer", e);
		}
	}

	public void startServer() {
		startWriter();
		startReader();
	}

	private void startReader() {
		logger.info("reader is started");
		try {
			while (true) {
				logger.info("요청을 기다리는중");
				selector.select();

				Iterator<SelectionKey> iter = selector.selectedKeys()
						.iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					if (key.isReadable()) {
						read(key);
					}
					iter.remove();
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "simpleChatClient.startServer", e);
		}
	}

	private void read(SelectionKey key) {
		SocketChannel sc = (SocketChannel) key.channel();
		// ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		ByteBuffer buffer = bufferPool.getMemoryBuffer();
		int read = 0;
		try {
			read = sc.read(buffer);
			logger.info(read + "byte read");
		} catch (IOException e) {
			try {
				sc.close();
			} catch (Exception e1) {

			}
		}
		buffer.flip();

		String data = "";
		try {
			data = decoder.decode(buffer).toString();
		} catch (CharacterCodingException e) {
			logger.log(Level.WARNING, "SimpleChatClient.read()");
		}

		System.out.println("message from server:" + data);
		clearBuffer(buffer);
	}

	private void clearBuffer(ByteBuffer buffer) {
		if (null != buffer) {
			// buffer.clear();
			// buffer = null;
			bufferPool.putBuffer(buffer);
		}
	}

	private void startWriter() {
		logger.info("writer is started");
		Thread t = new MyThread(sc);
		t.start();
	}

	class MyThread extends Thread {
		public MyThread(SocketChannel sc) {
		}

		public void run() {
			// ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
			ByteBuffer buffer = bufferPool.getMemoryBuffer();
			try {
				while (!Thread.currentThread().isInterrupted()) {
					buffer.clear();
					BufferedReader in = new BufferedReader(
							new InputStreamReader(System.in));
					String message = in.readLine();

					if (message.equals("quit") || message.equals("shutdown")) {
						System.exit(0);
					}

					buffer.put(message.getBytes());
					buffer.flip();

					sc.write(buffer);
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, "mythread.run()");
			} finally {
				clearBuffer(buffer);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Exam15_2SimpleChatClient client = new Exam15_2SimpleChatClient();
		client.initServer();
		client.startServer();
	}
}
