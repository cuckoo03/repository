package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Exam14_1StreamTest {
	static FileInputStream in = null;

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException,
			IOException {
		// TODO Auto-generated method stub
		in = new FileInputStream("data.bin");
		TestThread t = new TestThread(in);
		t.start();

		Thread.sleep(2000);
		System.out.println("avai:" + in.available());

		t.interrupt();

		Thread.sleep(2000);
		System.out.println("avai2:" + in.available());
	}

	static class TestThread extends Thread {
		FileInputStream in = null;

		public TestThread(FileInputStream o) {
			this.in = o;
		}

		public void run() {
			try {
				int v = 0;
				System.out.println("thread start");
				while ((v = in.read()) != -1) {
					System.out.println(v);
					Thread.sleep(1000);
				}
				in.close();
			} catch (Exception e) {
				System.out.println("thread end");
			}
		}
	}
}
