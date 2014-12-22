package com.thread.ch06_exam6_4;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database<V, K> {
	private final Map<K, V> map = new HashMap<K, V>();
	private final ReadWriteLock lock = new ReentrantReadWriteLock(true /* fair */);
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();

	public void clear() {
		writeLock.lock();
		try {
			verySlow();
			map.clear();
		} finally {
			writeLock.unlock();
		}
	}

	public void assign(K key, V value) {
		writeLock.lock();
		try {
			verySlow();
			map.put(key, value);
		} finally {
			writeLock.unlock();
		}
	}

	public V retrieve(K key) {
		readLock.lock();
		try {
			slowly();
			return map.get(key);
		} finally {
			readLock.unlock();
		}
	}

	private void slowly() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void verySlow() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}