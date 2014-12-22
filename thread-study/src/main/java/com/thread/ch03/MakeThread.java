package com.thread.ch03;

import java.util.HashMap;
import java.util.Vector;

public class MakeThread {

	public static void main(String[] args) {
		for (int loop = 0; loop < 3; loop++) {
			LoopingThread thread = new LoopingThread();
			thread.start();
		}
		System.out.println("Started looping thread.");
	}
}

class LoopingThread extends Thread {
	@Override
	public void run() {
		int runcount = 100;
		while (true) {
			try {

				String string = new String("AAA");
				Vector<String> list = new Vector<String>(runcount);
				for (int loop = 0; loop < runcount; loop++) {
					list.add(string);
				}

				HashMap<String, Integer> map = new HashMap<String, Integer>(
						runcount);
				for (int loop = 0; loop < runcount; loop++) {
					map.put(string + loop, loop);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
