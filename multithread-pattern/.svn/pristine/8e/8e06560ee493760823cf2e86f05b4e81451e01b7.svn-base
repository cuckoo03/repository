package com.thread.ch01.exam06;

public class List1_14Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("tesing eaterthread");
		Tool spoon = new Tool("Spoon");
		Tool fork = new Tool("Fork");
		new EaterThread("Alice", spoon, fork).start();
		new EaterThread("Bobby", fork, spoon).start();
	}

}
