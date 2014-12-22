package com.thread.ch10_exam10_3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CountupThread extends Thread {
	private long counter = 0;
	private volatile boolean shutdownRequested;
	private InterruptedException savedException;

	public void run() {
		while (!isShutdownRequested()) {
			try {
				doWork();
			} catch (InterruptedException e) {
				e.printStackTrace();
				savedException = e;
			} finally {
				doShutdown();
			}
		}
	}

	public void shutdownRequest() {
		shutdownRequested = true;
		interrupt();
	}

	public boolean isShutdownRequested() {
		return shutdownRequested;
	}

	private void doShutdown() {
		System.out.println("doShutdown: counter = " + counter);

		if (null != savedException) {
			File file = new File("counter.txt");
			try {
				FileOutputStream fos = new FileOutputStream(file);
				byte[] b = String.valueOf(counter).getBytes();
				fos.write(b);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void doWork() throws InterruptedException {
		counter++;
		System.out.println("doWork counter = " + counter);
		Thread.sleep(1000);
	}
}
