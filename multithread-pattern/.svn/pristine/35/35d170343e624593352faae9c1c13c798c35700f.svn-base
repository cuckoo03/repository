package com.thread.ch05_exam5_5;

import com.thread.ch05_01.Table;

public class ClearThread implements Runnable {
	private Table table;

	public ClearThread(Table table) {
		this.table = table;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				table.clear();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
