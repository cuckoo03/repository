package com.pecktpub.java7.concurrency.chap3.multiplecopies;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue {
	private final Semaphore semaphore;
	private boolean freePrinters[];
	private Lock lockPrinters;

	public PrintQueue() {
		this.semaphore = new Semaphore(2);
		freePrinters = new boolean[2];
		for (int i = 0; i <freePrinters.length; i++) {
			freePrinters[i] = true;
		}
		lockPrinters = new ReentrantLock();
	}

	public void printJob(Object document) {
		try {
			semaphore.acquire();
			int assignedPrinter = getPrinter();
			
			long duration = (long) (Math.random() * 10);
			System.out.println(Thread.currentThread().getName() + ", assigned printer:"
					+ assignedPrinter + ", duration:" + duration);
			TimeUnit.SECONDS.sleep(duration);
			
			freePrinters[assignedPrinter] = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
	}

	private int getPrinter() {
		int ret = -1;
		try {
			lockPrinters.lock();
			
			for (int i = 0; i < freePrinters.length; i++) {
				if (freePrinters[i]) {
					ret = i;
					freePrinters[i] = false;
					break;
				}
			}
			System.out.println("printer ready");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lockPrinters.unlock();
		}
		return ret;
	}
}