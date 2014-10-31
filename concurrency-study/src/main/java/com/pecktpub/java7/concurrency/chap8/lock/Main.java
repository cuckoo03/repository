package com.pecktpub.java7.concurrency.chap8.lock;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Monitoring a Lock interface
 * 
 * @author cuckoo03
 *
 */
public class Main {

	public static void main(String[] args) throws InterruptedException {
		MyLock lock = new MyLock();
		Thread threads[] = new Thread[5];

		for (int i = 0; i < 5; i++) {
			Task task = new Task(lock);
			threads[i] = new Thread(task);
			threads[i].start();
		}

		for (int i = 0; i < 15; i++) {
			System.out.println("Main:Logging the Lock");
			System.out.println("*****");
			System.out.println("Lock Owner:" + lock.getOwnerName());
			System.out.println("Queued Threads:" + lock.hasQueuedThreads());
			if (lock.hasQueuedThreads()) {
				System.out
						.println("Lock:Queue length:" + lock.getQueueLength());
				System.out.println("Lock:Queued threads");
				Collection<Thread> lockedThreads = lock.getThreads();
				for (Thread lockedThread:lockedThreads) {
					System.out.print(lockedThread.getName() + ",");
				}
			}
			System.out.println("");
			System.out.println("Lock:Fairness:" + lock.isFair());
			System.out.println("Lock:Locked:" + lock.isLocked());
			System.out.println("*****");
			
			TimeUnit.SECONDS.sleep(1);
		}
	}

}
