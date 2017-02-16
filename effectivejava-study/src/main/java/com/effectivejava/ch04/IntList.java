package com.effectivejava.ch04;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

public class IntList {
	static List intArrayAsList(final int[] a) {
		if (a == null)
			throw new NullPointerException();
		return new AbstractList() {
			@Override
			public Object get(int index) {
				return new Integer(a[index]);
			}

			@Override
			public int size() {
				return a.length;
			}

			public Object set(int i, Object o) {
				int oldVal = a[i];
				a[i] = ((Integer) o).intValue();
				return new Integer(oldVal);
			}
		};
	}

	public static void main(String[] args) {
		int n = 10;
		int a[] = new int[n];
		for (int i = 0; i < n; i++)
			a[i] = i;
		List l = intArrayAsList(a);
		Collections.shuffle(l);
		System.out.println("List element:" + l);
		Collections.sort(l);
		System.out.println("List element:" + l);
	}
}
