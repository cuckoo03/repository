package com.ch16;

import java.io.File;
import java.io.IOException;

import com.ch16.pool.PoolManager;
import com.ch16.pool.buffer.ByteBufferPool;
import com.ch16.pool.buffer.ByteBufferPoolIF;
import com.ch16.pool.selector.AcceptSelectorPool;
import com.ch16.pool.selector.RequestSelectorPool;
import com.ch16.pool.selector.SelectorPoolIF;
import com.ch16.pool.thread.ThreadPool;
import com.ch16.pool.thread.ThreadPoolIF;
import com.ch16.queue.BlockingEventQueue;
import com.ch16.queue.Queue;

public class AdvancedChatServer {
	private Queue queue = null;
	private SelectorPoolIF acceptSelectorPool = null;
	private SelectorPoolIF requestSelectorPool = null;

	private ByteBufferPoolIF byteBufferPool = null;

	private ThreadPoolIF acceptThreadPool = null;
	private ThreadPoolIF readWriteThreadPool = null;

	public AdvancedChatServer() {
		try {
			initResource();
			startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startServer() {
		acceptThreadPool.startAll();
		readWriteThreadPool.startAll();

		acceptSelectorPool.startAll();
		requestSelectorPool.startAll();
	}

	private void initResource() throws IOException {
		File bufferFile = new File("Buffer.tmp");
		if (!bufferFile.exists()) {
			bufferFile.createNewFile();
		}
		bufferFile.deleteOnExit();
		byteBufferPool = new ByteBufferPool(20 * 1024, 40 * 2048, bufferFile);

		queue = BlockingEventQueue.getInstance();

		PoolManager.getInstance().registByteBufferPool(byteBufferPool);

		acceptThreadPool = new ThreadPool(queue,
				"com.ch16.pool.thread.processor.AcceptProcessor");
		readWriteThreadPool = new ThreadPool(queue,
				"com.ch16.pool.thread.processor.ReadWriteProcessor");

		acceptSelectorPool = new AcceptSelectorPool(queue);
		requestSelectorPool = new RequestSelectorPool(queue);

		PoolManager.getInstance().registAcceptSelectorPool(acceptSelectorPool);
		PoolManager.getInstance()
				.registRequestSelectorPool(requestSelectorPool);
	}

	public static void main(String[] args) {
		new AdvancedChatServer();
	}
}
