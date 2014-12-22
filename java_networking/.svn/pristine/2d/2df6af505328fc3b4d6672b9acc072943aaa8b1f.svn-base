package com.thread.ch9_exam9_4;

import java.util.concurrent.ExecutionException;

public class FutureData implements Data {
	private RealData realdata;
	private boolean ready = false;
	private ExecutionException exception;

	public synchronized void setRealData(RealData realdata) {
		if (ready) {
			return;
		}
		this.realdata = realdata;
		this.ready = true;
		System.out.println("notifyAll");
		notifyAll();
	}

	public synchronized String getContent() throws ExecutionException {
		while (!ready) {
			try {
				System.out.println("wait");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (null != exception) {
			throw exception;
		}
		return realdata.getContent();
	}

	public synchronized void setException(Exception e) {
		if (ready) {
			return;
		}
		this.exception = new ExecutionException(e);
		this.ready = true;
		notifyAll();
	}
}
