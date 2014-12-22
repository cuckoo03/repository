package com;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Exam15_1SimpleChatServer {
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 10001;

	private static FileHandler fileHandler;
	private static Logger logger = Logger.getLogger("net.daum.javacafe");

	private Selector selector = null;
	private ServerSocketChannel serverSocketChannel = null;
	private ServerSocket serverSocket = null;

	private List<SocketChannel> room = new Vector<SocketChannel>();

	private Exam14_7ByteBufferPool bufferPool = null;

	public void initServer() {
		try {
			selector = Selector.open();

			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocket = serverSocketChannel.socket();

			InetSocketAddress isa = new InetSocketAddress(HOST, PORT);
			serverSocket.bind(isa);

			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			bufferPool = new Exam14_7ByteBufferPool(2048, 2048, new File(
					"data.bin"));
		} catch (Exception e) {
			logger.log(Level.WARNING, "SimpleChartServer.initServer()", e);
		}
	}

	public void startServer() {
		logger.info("server is started");
		try {
			while (true) {
				logger.info("요청을 기다리는중");
				selector.select();

				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					if (key.isAcceptable()) {
						accept(key);
					} else if (key.isReadable()) {
						read(key);
					}
					it.remove();
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "SimpleChartServer.startServer()", e);
		}
	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel sc = (SocketChannel) key.channel();
		// ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		ByteBuffer buffer = bufferPool.getMemoryBuffer();
		try {
			// bufferPool.putBuffer(buffer);
			int read = sc.read(buffer);
			logger.info(read + "byte를 읽었습니다");
		} catch (IOException e) {
			try {
				sc.close();
			} catch (IOException e1) {

			}
			removeUser(sc);
			logger.info(sc.toString() + "클라이언트가 접속을 해제했습니다");

		}
		try {
			broadcast(buffer);
		} catch (Exception e2) {
			logger.log(Level.WARNING, "simpleChatServer.broadcast()", e2);
		}
		clearBuffer(buffer);
	}

	private void clearBuffer(ByteBuffer buffer) {
		if (null != buffer) {
			bufferPool.putBuffer(buffer);
		}
	}

	private void broadcast(ByteBuffer buffer) throws IOException {
		buffer.flip();

		Iterator<SocketChannel> iter = room.iterator();
		while (iter.hasNext()) {
			SocketChannel sc = iter.next();
			if (null != sc) {
				sc.write(buffer);
				buffer.rewind();
			}
		}
	}

	private void removeUser(SocketChannel sc) {
		room.remove(sc);
	}

	private void accept(SelectionKey key) {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		SocketChannel sc;
		try {
			sc = server.accept();
			registerChannel(selector, sc, SelectionKey.OP_READ);
			logger.info(sc.toString() + " 클라이언트가 접속했습니다");
		} catch (ClosedChannelException e) {
			logger.log(Level.WARNING, "SimpleChartServer.accept()", e);
		} catch (IOException e) {
			logger.log(Level.WARNING, "SimpleChartServer.accept()", e);
		}
	}

	private void registerChannel(Selector selector2, SocketChannel sc,
			int opRead) throws IOException {
		if (null == sc) {
			logger.info("invalid connection");
			return;
		}
		sc.configureBlocking(false);
		sc.register(selector, opRead);
		addUser(sc);
	}

	private void addUser(SocketChannel sc) {
		room.add(sc);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Exam15_1SimpleChatServer scs = new Exam15_1SimpleChatServer();
		scs.initServer();
		scs.startServer();
	}

}
