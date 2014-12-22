package com.thread.ch7_exam7_4_1;

public class Helper {
	public void handle(int count, char c) {
		System.out.println("Handle(" + count + ", " + c + ") Begin");
		for (int i = 0; i < count; i++) {
			slowly();
			System.out.println(c);
		}
		System.out.println("");
		System.out.println("Handle(" + count + ", " + c + ") End");
	}

	private void slowly() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
