package com.thread.ch8_exam8_6_1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * ThreadPoolExecutor를 사용하여 구성하기
 * 
 * @author cuckoo03
 * 
 */
public class Main {
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		try {
			Channel channel = new Channel(3);
			channel.startWorkers(executorService);
			ThreadFactory factory = Executors.defaultThreadFactory();
			factory.newThread(new ClientThread("A", channel)).start();
			factory.newThread(new ClientThread("B", channel)).start();
			factory.newThread(new ClientThread("C", channel)).start();

			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Main end");
			executorService.shutdown();
		}
	}

}
