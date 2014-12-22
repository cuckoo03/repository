package com;

public class Exam14_1_1InterruptableChannel {

	/**
	 * interruptableChannel을 사용시 스레드 관련된 Exception예제
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	}
}

class CloseExceptionThread extends Thread {
	public void run() {
		while (true) {
			try {
				// cost가 큰 작업
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}