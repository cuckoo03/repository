package com.pecktpub.java7.concurrency.chap3.synctasks;

import java.util.concurrent.CyclicBarrier;

public class Searcher implements Runnable {
	private int firstRow;
	private int lastRow;
	private MatrixMock mock;
	private Results results;
	private int number;
	private final CyclicBarrier barrier;

	public Searcher(int firstRow, int lastRow, MatrixMock mock,
			Results results, int number, CyclicBarrier barrier) {
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.mock = mock;
		this.results = results;
		this.number = number;
		this.barrier = barrier;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()
				+ " Processing lines from:" + firstRow + "," + lastRow);
		
		for (int i = firstRow; i < lastRow; i++) {
			// 작성중
		}
	}

}
