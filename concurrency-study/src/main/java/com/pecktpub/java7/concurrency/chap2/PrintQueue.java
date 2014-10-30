package com.pecktpub.java7.concurrency.chap2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue {
	private Lock queueLock = new ReentrantLock();
}
