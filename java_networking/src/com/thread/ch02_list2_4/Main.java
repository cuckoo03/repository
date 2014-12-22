package com.thread.ch02_list2_4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
//		list = Collections
//				.synchronizedList(list);
		list = new CopyOnWriteArrayList<Integer>(list);
		
		ThreadFactory factory = Executors.defaultThreadFactory();

		factory.newThread(new WriterThread(list)).start();
		factory.newThread(new ReaderThread(list)).start();

	}

}
