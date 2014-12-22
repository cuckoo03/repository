package com.thread.ch06_exam6_6;

public class ReadWriteLock {
	private int readingReaders = 0;
//	private int waitingWriters = 0;
	private int writingWriters = 0;
//	private boolean preferWriter = true;

	public synchronized void readLock() throws InterruptedException {
		while (writingWriters > 0 /*|| (preferWriter && waitingWriters > 0) */) {
			wait();
		}
		readingReaders++;
	}

	public synchronized void readUnlock() {
		readingReaders--;
//		preferWriter = true;
		notifyAll();
	}

	public synchronized void writeLock() throws InterruptedException {
//		waitingWriters++;
		try {
			while (readingReaders > 0 || writingWriters > 0) {
				wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			waitingWriters--;
		}
		writingWriters++;
	}

	public synchronized void writeUnlock() {
		writingWriters--;
//		preferWriter = false;
		notifyAll();
	}

}
