package com.pecktpub.java7.concurrency.chap6.nonblockinglist;

import java.util.concurrent.ConcurrentLinkedDeque;

public class PollTask implements Runnable{
	private ConcurrentLinkedDeque<String> list;
	public PollTask(ConcurrentLinkedDeque<String> list) {
		this.list = list;
	}

	@Override
	public void run() {
		for (int i = 0; i < 5000; i++) {
			String f = list.pollFirst();
			String l = list.pollLast();
//			System.out.println("f:" + f + ",l:" + l);
		}
	}

}
