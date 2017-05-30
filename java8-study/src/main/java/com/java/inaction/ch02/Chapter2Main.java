package com.java.inaction.ch02;

import java.util.ArrayList;
import java.util.List;

public class Chapter2Main {
	public static void printApple(List<Apple> apples, ApplePredicate predicate) {
		for (Apple apple : apples) {
			if (predicate.test(apple))
				System.out.println(apple.getWeight());
		}
	}

	public static <T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> result = new ArrayList<>();
		for (T e : list) {
			if (p.test(e))
				result.add(e);
		}

		return result;
	}

	public static void main(String[] args) {
		List<Apple> apples = new ArrayList<>();
		apples.add(new Apple(1));
		apples.add(new Apple(151));
		apples.add(new Apple(161));
		printApple(apples, new AppleHeavyWeightPredicate());

		printApple(apples, (Apple apple) -> apple.getWeight() > 1);

		List<Apple> apples2 = filter(apples,
				(Apple apple) -> apple.getWeight() > 1);
		for (Apple apple : apples2)
			System.out.println(apple.getWeight());

		new Thread(() -> System.out.println("thread")).start();
	}
}
