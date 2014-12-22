package com.thread.ch06_list6_1;

public class Data {
	private final char[] buffer;
	private ReadWriteLock lock = new ReadWriteLock();

	public Data(int size) {
		buffer = new char[size];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = '*';
		}
	}

	public char[] read() throws InterruptedException {
		lock.readLock();
		try {
			return doRead();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.readUnlock();
		}
		return null;
	}

	public void write(char c) throws InterruptedException {
		lock.writeLock();
		try {
			doWrite(c);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.writeUnlock();
		}
	}

	private char[] doRead() throws InterruptedException {
		char[] newBuffer = new char[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			newBuffer[i] = buffer[i];
		}
		slowly();
		return newBuffer;
	}

	private void doWrite(char c) throws InterruptedException {
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = c;
			slowly();
		}
	}

	private void slowly() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
