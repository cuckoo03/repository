package com;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Exam14_7ByteBufferPool {
	private static final int MEMORY_BLOCKSIZE = 1024;
	private static final int FILE_BLOCKSIZE = 2048;

	private final List<ByteBuffer> memoryQueue = new ArrayList<ByteBuffer>();
	private final List<ByteBuffer> fileQueue = new ArrayList<ByteBuffer>();

	private boolean isWait = false;

	public Exam14_7ByteBufferPool(int memorySize, int fileSize, File file)
			throws IOException {
		if (memorySize > 0) {
			initMemoryBuffer(memorySize);
		}
		if (fileSize > 0) {
			initFileBuffer(fileSize, file);
		}
	}

	private void initMemoryBuffer(int memorySize) {
		int bufferCount = memorySize / MEMORY_BLOCKSIZE;
		memorySize = bufferCount * MEMORY_BLOCKSIZE;
		ByteBuffer directBuf = ByteBuffer.allocateDirect(memorySize);
		divideBuffer(directBuf, MEMORY_BLOCKSIZE, memoryQueue);
	}

	private void divideBuffer(ByteBuffer directBuf, int memoryBlocksize,
			List<ByteBuffer> memoryQueue2) {
		int bufferCount = directBuf.capacity() / memoryBlocksize;
		int position = 0;
		for (int i = 0; i < bufferCount; i++) {
			int max = position + memoryBlocksize;
			directBuf.limit(max);
			memoryQueue2.add(directBuf.slice());
			position = max;
			directBuf.position(position);
		}
	}

	private void initFileBuffer(int size, File f) throws IOException {
		int bufferCount = size / FILE_BLOCKSIZE;
		size = bufferCount * FILE_BLOCKSIZE;
		RandomAccessFile file = new RandomAccessFile(f, "rw");
		try {
			file.setLength(size);
			ByteBuffer fileBuffer = file.getChannel().map(
					FileChannel.MapMode.READ_WRITE, 0L, size);
			divideBuffer(fileBuffer, FILE_BLOCKSIZE, fileQueue);
		} finally {
			file.close();
		}
	}

	private ByteBuffer getBuffer(List<ByteBuffer> firstQueue,
			List<ByteBuffer> secondQueue) {
		ByteBuffer buffer = getBuffer(firstQueue, false);
		if (null == buffer) {
			buffer = getBuffer(secondQueue, false);
			if (null == buffer) {
				buffer = getBuffer(firstQueue, true);
			} else {
				buffer = ByteBuffer.allocate(MEMORY_BLOCKSIZE);
			}
		}
		return buffer;
	}

	private ByteBuffer getBuffer(List<ByteBuffer> queue, boolean wait) {
		synchronized (queue) {
			if (queue.isEmpty()) {
				if (wait) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
						return null;
					}
				} else {
					return null;
				}
			}
		}
		return queue.remove(0);
	}

	public void putBuffer(ByteBuffer buffer) {
		if (buffer.isDirect()) {
			switch (buffer.capacity()) {
			case MEMORY_BLOCKSIZE: {
				putBuffer(buffer, memoryQueue);
				break;
			}
			case FILE_BLOCKSIZE: {
				putBuffer(buffer, memoryQueue);
				break;
			}
			}
		}
	}

	private void putBuffer(ByteBuffer buffer, List<ByteBuffer> queue) {
		buffer.clear();
		synchronized (queue) {
			queue.add(buffer);
			queue.notify();
		}
	}

	public ByteBuffer getMemoryBuffer() {
		return getBuffer(memoryQueue, fileQueue);
	}

	public ByteBuffer getFileBuffer() {
		return getBuffer(fileQueue, memoryQueue);
	}

	public synchronized void setWait(boolean wait) {
		this.isWait = wait;
	}

	public synchronized boolean isWait() {
		return isWait;
	}
}
