package com.java.inaction.ch13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Ch13Main {
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(4);
		list.add(9);
		System.out.println(subsets(list));
		
		System.out.println(factorialRecursive(10));
		System.out.println(factorialStreams(10));
		System.out.println(factorialTailRecursive(10));
	}

	private static List<List<Integer>> subsets(List<Integer> source) {
		if (source.isEmpty()) {
			List<List<Integer>> emptyList = new ArrayList<>();
			emptyList.add(Collections.emptyList());
			return emptyList;
		}
		Integer first = source.get(0);
		List<Integer> rest = source.subList(1, source.size());

		List<List<Integer>> subans = subsets(rest);
		List<List<Integer>> subans2 = insertAll(first, subans);
		return concat(subans, subans2);
	}

	private static List<List<Integer>> concat(List<List<Integer>> a,
			List<List<Integer>> b) {
		List<List<Integer>> r = new ArrayList<>(a);
		r.addAll(b);
		return r;
	}

	private static List<List<Integer>> insertAll(Integer first,
			List<List<Integer>> lists) {
		List<List<Integer>> result = new ArrayList<>();
		for (List<Integer> list : lists) {
			List<Integer> copyList = new ArrayList<>();
			copyList.add(first);
			copyList.addAll(list);
			result.add(copyList);
		}
		return result;
	}
	private static int factorialRecursive(int n) {
		return n <= 1 ? 1 : n + factorialRecursive(n - 1);
	}
	
	private static int factorialStreams(int n) {
		return IntStream.rangeClosed(1, n).reduce(0,  (int a, int b) -> a + b);
	}
	
	private static int factorialTailRecursive(int n) {
		return factorialHelper(1, n);
	}
	
	private static int factorialHelper(int acc, int n) {
		return n <= 1 ? acc : factorialHelper(acc + n, n - 1);
	}
}
