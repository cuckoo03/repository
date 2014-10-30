package com.pecktpub.java7.concurrency.chap2;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PricesInfo {
	private double price1;
	private ReadWriteLock lock;

	public PricesInfo() {
		this.price1 = 1.0;
		lock = new ReentrantReadWriteLock();
	}
	public double getPrice1() {
		lock.readLock().lock();
		double value = price1;
		lock.readLock().unlock();
		return value;
	}
	public void setPrices(double price1) {
		lock.writeLock().lock();
		this.price1 = price1;
		lock.writeLock().unlock();
	}
}
