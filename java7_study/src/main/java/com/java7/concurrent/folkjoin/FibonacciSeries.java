package com.java7.concurrent.folkjoin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FibonacciSeries {

	public static void main(String[] args) {
		int size = 44;
		Long startTime = Calendar.getInstance().getTimeInMillis();
		List<Integer> fibonacciSeries = new ArrayList<>();
		for (int index = 0; index < size; index++) {
			fibonacciSeries.add(FibonacciGenerator.generate(index));
		}
		Long endTime = Calendar.getInstance().getTimeInMillis();
		System.out.println(endTime - startTime);
		dumpList(fibonacciSeries);
	}

	private static void dumpList(List<Integer> fibonnacciSeries) {
		int index = 0;
		for (Object object : fibonnacciSeries) {
			System.out.printf("%d\t%d\n", index++, object);
		}

	}

	public static class FibonacciGenerator {
		public static Integer generate(Integer index) {
			if (0 == index) {
				return 0;
			}
			if (index < 2) {
				return 1;
			}
			Integer result = generate(index - 1) + generate(index - 2);
			return result;
		}
	}
}
