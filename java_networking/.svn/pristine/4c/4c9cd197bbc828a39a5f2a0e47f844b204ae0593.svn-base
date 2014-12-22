package com.thread.ch9_exam9_3;

public class ASyncContentImpl implements Content {
	private SyncContentImpl realdata;
	private boolean ready;

	public synchronized byte[] getBytes() {
		while (!ready) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return realdata.getBytes();
	}

	public synchronized void setRealData(SyncContentImpl realdata) {
		if (ready) {
			return;
		}

		this.realdata = realdata;
		this.ready = true;
		notifyAll();
	}
}
