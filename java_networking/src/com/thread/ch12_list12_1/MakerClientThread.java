package com.thread.ch12_list12_1;

public class MakerClientThread extends Thread {

	private ActiveObject activeObject;
	private char fillchar;

	public MakerClientThread(String name, ActiveObject activeObject) {
		super(name);
		this.activeObject = activeObject;
		this.fillchar = name.charAt(0);
	}

	public void run() {
		try {
			for (int i = 0; true; i++) {
				Result<String> result = activeObject.makeString(i, fillchar);
				Thread.sleep(10);
				String value = result.getResultValue();
				System.out.println(Thread.currentThread().getName()
						+ ": value=" + value);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
