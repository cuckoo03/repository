package com.java.inaction.ch13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ch13Main {
	public static void main(String[] args) {

	}

	static List<List<Integer>> subsets(List<Integer> list) {
		if (list.isEmpty()) {
			List<List<Integer>> ans = new ArrayList<>();
			ans.add(Collections.emptyList());
			return ans;
		}
		Integer first = list.get(0);
		List<Integer> rest = list.subList(1, list.size());

		List<List<Integer>> subans = subsets(rest);
		List<List<Integer>> subans2 = insertAll(first, subans);
		return concat(subans, subans2);
	}

	private static List<List<Integer>> concat(List<List<Integer>> subans,
			List<List<Integer>> subans2) {
		// TODO Auto-generated method stub
		return null;
	}

	private static List<List<Integer>> insertAll(Integer first,
			List<List<Integer>> subans) {
		// TODO Auto-generated method stub
		return null;
	}
}
