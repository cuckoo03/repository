package com.java.lamdas;

public class Main {
	public static void main(String[] args) {

		IAddable<String> adder = (String s1, String s2) -> s1 + s2;
		IAddable<Integer> square = (i1, i2) -> i1 * i2;

		System.out.println(adder.add("1", "2"));
		System.out.println(square.add(1, 2));

		testPreJava8();
		testJava8();
		testRunnable();

	}

	private static void testRunnable() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("prejava thread:" + Thread.currentThread());
			}
		}).start();
		new Thread(() -> System.out.println("java8 thread:"
				+ Thread.currentThread())).start();
	}

	private static void testJava8() {
		IAddable<Trade> aggregatedQty = (t1, t2) -> {
			t1.setQuantity(t1.getQuantity() + t2.getQuantity());
			return t1;
		};
		Trade t1 = new Trade(1);
		Trade t2 = new Trade(2);
		Trade t3 = aggregatedQty.add(t1, t2);
		System.out.println(t3.getQuantity());
	}

	// implements previous java8
	public static void testPreJava8() {
		IAddable<Trade> tradeMerger = new IAddable<Trade>() {
			@Override
			public Trade add(Trade t1, Trade t2) {
				t1.setQuantity(t1.getQuantity() + t2.getQuantity());
				return t1;
			}
		};
		Trade t1 = new Trade(1);
		Trade t2 = new Trade(2);
		Trade mergedTrade = tradeMerger.add(t1, t2);
		System.out.println(mergedTrade.getQuantity());
	}
}