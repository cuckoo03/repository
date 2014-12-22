package com.thread.ch4;

import java.util.concurrent.TimeoutException;

public class List4_7Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Host host = new Host(10000);
		try {
			System.out.println("execute begin");
			host.execute();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
