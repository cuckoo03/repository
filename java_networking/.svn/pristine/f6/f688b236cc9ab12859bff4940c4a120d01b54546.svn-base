package com.corejava.chap001;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class MapTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map sortedMap = new TreeMap();
		sortedMap.put("C", "2");
		sortedMap.put("A", "1");
		sortedMap.put("a", "3");

		Set set = sortedMap.entrySet();
		Iterator iter = set.iterator();
		while (iter.hasNext()) {
			Map.Entry ent = (Entry) iter.next();
			System.out.println(ent.getKey() + ":" + ent.getValue());
		}
		Set hashset = new HashSet();
		List linkedlist = new LinkedList();
	}
}
