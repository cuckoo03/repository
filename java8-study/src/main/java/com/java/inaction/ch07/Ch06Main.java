package com.java.inaction.ch07;

import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Ch06Main {
	public static long measureSumPerf(Function<Long, Long> adder, long n) {
		long fastest = Long.MAX_VALUE;
		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			long sum = adder.apply(n);
			long duration = (System.nanoTime() - start) / 1_000_000;
			System.out.println("result:" + sum);
			if (duration < fastest)
				fastest = duration;
		}
		return fastest;
	}

	public static int countWords(Stream<Character> stream) {
		WordCounter wordCounter = stream.parallel().reduce(
				new WordCounter(0, true), WordCounter::accumulate,
				WordCounter::combile);
		return wordCounter.getCounter();
	}

	public static void main(String[] args) {
		System.out.println(ParallelStreams.sequentialSum(10L));

		System.out.println("sequential sum done:"
				+ measureSumPerf(ParallelStreams::sequentialSum, 10_000_000)
				+ "mecs");
		System.out.println("iterative sum done:"
				+ measureSumPerf(ParallelStreams::iterativeSum, 10_000_000)
				+ "mecs");
		System.out.println("parallel sum done:"
				+ measureSumPerf(ParallelStreams::parallelSum, 10_000_000)
				+ "mecs");
		System.out.println("range sum done:"
				+ measureSumPerf(ParallelStreams::rangedSum, 10_000_000)
				+ "mecs");
		System.out.println("sideEffect sum done:"
				+ measureSumPerf(ParallelStreams::sideEffectSum, 10_000_000)
				+ "mecs");
		System.out.println("sideEffectParallel sum done:"
				+ measureSumPerf(ParallelStreams::sideEffectParallelSum,
						10_000_000) + "mecs");

		// recursiveTask
		System.out
				.println("forkJoinSum done:"
						+ measureSumPerf(ForkJoinSumCalculator::forkJoinSum,
								10_000_000) + "msecs");
		
		// spliterator
		final String SENTENCE = "Nel mezzo del cammin di nostra vita"
				+ "mi r i trovai in una selva oscura"
				+ " ch la dritta via era smarrita ";
		Stream<Character> stream = IntStream.range(0, SENTENCE.length())
				.mapToObj(SENTENCE::charAt);
		System.out.println(countWords(stream) + " word");
		
		// spliterator
		Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);
		Stream<Character> stream2 = StreamSupport.stream(spliterator, true); 
		System.out.println("found:" + countWords(stream2) + "word");
		
	}
}
