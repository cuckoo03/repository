package com.java.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class SortExam {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("2");
		list.add("3");
		list.add("1");
		list.add("A");
		Collections.sort(list);

		for (String item : list) {
			System.out.println(item);
		}

		List<NaturalOrderingSort> list2 = new ArrayList<NaturalOrderingSort>();
		NaturalOrderingSort map = new NaturalOrderingSort("1");
		list2.add(map);

		map = new NaturalOrderingSort("3");
		list2.add(map);

		map = new NaturalOrderingSort("2");
		list2.add(map);

		for (NaturalOrderingSort item : list2) {
			System.out.println(item.getValue());
		}
	}
}

class NaturalOrderingComparator implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {

		return 0;
	}
}

class NaturalOrderingSort implements Comparable<String> {
	private String value;

	public NaturalOrderingSort(String value) {
		super();
		this.value = value;
	}

	public int compareTo(String o) {
		System.out.println("compare:" + o);
		return 1;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
