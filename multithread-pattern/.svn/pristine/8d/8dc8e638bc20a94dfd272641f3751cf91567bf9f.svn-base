package com.thread.ch02_list2_4;

import java.util.List;

public class ReaderThread implements Runnable {
	private List<Integer> list;

	public ReaderThread(List<Integer> list) {
		System.out.println("ReaderThread");
		this.list = list;
	}

	public void run() {
		while (true) {
			synchronized (list) {
				for (int n : list) {
					System.out.println(n);
				}
			}
		}
		// for 문으로 실행시 list.size가 0으로 표시됨
		/*
		while (true) {
			synchronized (list) {
				for (int i= 0; i < list.size(); i++) {
					System.out.println(i);
				}
			}
		}
		*/
	}
}
