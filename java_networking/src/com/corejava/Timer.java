package com.corejava;

import javax.naming.TimeLimitExceededException;

public class Timer extends Thread {
	TimerListener listener;
	
	Timer(TimerListener t) {
		listener = t;
	}
	public void run() {
		while (true) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listener.timeElapsed(this);
		}
	}
}
