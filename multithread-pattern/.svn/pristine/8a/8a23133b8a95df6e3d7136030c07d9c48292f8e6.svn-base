package com.thread.ch9_list9_1;

public class FutureData implements Data {
	private RealData realdata;
	private boolean ready = false;

	public synchronized void setRealData(RealData realdata) {
		if (ready) {
			return;
		}
		this.realdata = realdata;
		this.ready = true;
		notifyAll();
	}

	public synchronized String getContent() {
		while (!ready) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return realdata.getContent();
	}

}
