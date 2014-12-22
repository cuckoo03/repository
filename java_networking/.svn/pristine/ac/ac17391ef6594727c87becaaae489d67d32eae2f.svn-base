package com.thread.ch02.exam02_03;

class NotSynch {
	private final String name = "NotSynch";

	public String toString() {
		return "[" + name + "]";
	}
}

class Synch {
	private final String name = "Synch";

	public synchronized String toString() {
		return "[" + name + "]";
	}
}

public class Main {

	private static final long CALL_COUNT = 100000000L;

	public static void main(String[] args) {
		trial("NotSync", CALL_COUNT, new NotSynch());
		trial("Sync", CALL_COUNT, new Synch());
	}

	private static void trial(String msg, long count, Object obj) {
		System.out.println(msg + ": being");
		long start_time = System.currentTimeMillis();
		for (long i = 0; i < count; i++) {
			obj.toString();
		}
		System.out.println(msg + ": end");
		System.out.println("elapsed time: "
				+ (System.currentTimeMillis() - start_time));
	}

}
