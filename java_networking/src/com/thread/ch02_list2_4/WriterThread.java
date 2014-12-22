package com.thread.ch02_list2_4;

import java.util.List;

public class WriterThread implements Runnable {
	private List<Integer> list;

	public WriterThread(List<Integer> list) {
		System.out.println("WriterThread");
		this.list = list;
	}

	public void run() {
		for (int i = 0; true; i++) {
			list.add(i);
			list.remove(0);
		}
	}

}
