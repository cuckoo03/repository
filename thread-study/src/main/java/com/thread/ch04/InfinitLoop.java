package com.thread.ch04;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InfinitLoop {

	public static void main(String[] args) {
		Random random = new Random();
		while (true) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < 1000; i++) {
				String temp = "A";
				list.add(temp);
			}

			if (random.nextInt(100000000) == 1) {
				break;
			}
		}
	}

}
