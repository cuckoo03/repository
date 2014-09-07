package com.java.tjpl.ch11;

import java.util.ArrayList;
import java.util.List;

public class Ch11Main {
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public static <E> void main(String[] args) {
		Cell<E> cell = new Cell<E>(null, null);
		List<Integer> list = new ArrayList();
		list.add(1);
		list.add(2);
		System.out.println(cell.sum(list));
		
		//call generic 
		String s1 = "Hello";
		System.out.println(cell.passThrough(s1));
		System.out.println(cell.passThrough((Object)s1));
		System.out.println((String)cell.passThrough((Object)s1));
		
		List<String> l = new ArrayList<String>();
		Object o = passThrough(l);
		
		List<String> list1 = (List<String>) o;
		List<String> list2 = (List) o;
		List<?> list3 = (List) o;
		List<?> list4 = (List<?>) o;
		System.out.println(list1);
		System.out.println(list2);
		System.out.println(list3);
		System.out.println(list4);
	}
	static Object passThrough(Object o) {
		return o;
	}
}
class Cell<E> {
	private E element;
	private Cell<E> next;

	public Cell(E element, Cell<E> next) {
		this.element = element;
		this.next = next;
	}

	public Cell<E> get1() {
		return next;
	}

	public E get2() {
		return element;
	}
	// use wildcard
	static double sum(List<? extends Number> list) {
		double sum = 0.0;
		for (Number n : list) {
			sum += n.doubleValue();
		}
		return sum;
	}
	<T> T passThrough(T obj) {
		return obj;
	}
	
}
