package com.thread.ch12_list12_1;

public class Servant implements ActiveObject {

	public Result<String> makeString(int count, char fillchar) {
		char[] buffer = new char[count];
		for (int i = 0; i < count; i++) {
			buffer[i] = fillchar;

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return new RealResult<String>(new String(buffer));
	}

	public void displayString(String string) {
		try {
			System.out.println("displayString: " + string);
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
