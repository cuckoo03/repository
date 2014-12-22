package com.thread.ch01.exam05;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("main");
		for (int trial = 0; true; trial++) {
			SecurityGate gate = new SecurityGate();
			CrackerThread[] t = new CrackerThread[5];

			for (int i = 0; i < t.length; i++) {
				t[i] = new CrackerThread(gate);
				t[i].start();
			}

			for (int i = 0; i < t.length; i++) {
				try {
					t[i].join();
				} catch (InterruptedException e) {

				}
			}

			if (gate.getCounter() == 0) {
				System.out.println(".");
			} else {
				System.out.println("securitygate is not safe");
				System.out.println("getCounter=" + gate.getCounter());
				System.out.println("trial:" + trial);
				break;
			}
		}
	}

}
