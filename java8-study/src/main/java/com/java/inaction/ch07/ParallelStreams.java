package com.java.inaction.ch07;

import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParallelStreams {
	public static long iterativeSum(long n) {
		long result = 0;
		for (long i = 1L; i <= n; i++) {
			result += i;
		}
		return result;
	}

	public static long parallelSum(long n) {
		return Stream.iterate(1L, i -> i + 1).limit(n).parallel()
				.reduce(0L, Long::sum);
	}

	public static long sequentialSum(long n) {
		return Stream.iterate(1L, i -> i + 1).limit(n).sequential()
				.reduce(0L, Long::sum);
	}

	public static long rangedSum(long n) {
		return LongStream.rangeClosed(1, n).reduce(0L, Long::sum);
	}
	
	public static long sideEffectSum(long n) {
		Accumulator acc = new Accumulator();
		LongStream.rangeClosed(1,  n).forEach(acc::add);
		return acc.total;
	}
	public static long sideEffectParallelSum(long n) {
		Accumulator acc = new Accumulator();
		LongStream.rangeClosed(1,  n).parallel().forEach(acc::add);
		return acc.total;
	}
}
class Accumulator {
	public long total = 0;
	public void add(long value) {
		total += value;
	}
}
