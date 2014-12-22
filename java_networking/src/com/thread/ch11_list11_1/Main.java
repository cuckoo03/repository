package com.thread.ch11_list11_1;

public class Main {
	public static void main(String[] args) {
		System.out.println("begin");
		for (int i = 0; i < 10; i++) {
			Log.println("main : i =" + i);
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Log.close();
		System.out.println("end");
	}

}
