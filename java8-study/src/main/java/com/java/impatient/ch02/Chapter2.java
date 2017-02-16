package com.java.impatient.ch02;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Chapter2 {
	public static void main(String[] args) {
		String contents = "Aa,B,Cc";
		List<String> words = Arrays.asList(contents.split(","));
		long count = words.stream().filter(w -> w.length() > 1).count();
		System.out.println(count);

		Stream<String> words2 = Stream.of(contents.split(","));

		Stream<Double> randoms = Stream.generate(Math::random);
		
		Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO,
				n -> n.add(BigInteger.ONE));
		System.out.println(integers);

		Stream<String> lowercaseWords = words.stream().map(String::toLowerCase);
		

		Stream<Stream<Character>> result = words.stream().map(
				w -> characterStream(w));
		
		// stateful transformation
		Stream<String> unique = Stream.of("a", "b", "b").distinct(); 
//		unique.forEach(System.out::println);
		
		// reduction
		Optional<String> largest = unique.max(String::compareToIgnoreCase);
		if (largest.isPresent())
			System.out.println(largest.get());
		
		// option type
		largest.ifPresent(v->System.out.println(v));
		largest.ifPresent(System.out::println);
		Optional<String> added = largest.map(String::new);
		
		String result2 = largest.orElse("");
		String result3 = largest.orElseGet(()->System.getProperty("user.dir"));
		String result4 = largest.orElseThrow(NoSuchElementException::new);
		
		Optional nullOrValue = Optional.ofNullable("");
		
		// reduction
		Stream<Integer> values = Stream.of(1, 2, 3);
		Optional<Integer> sum = values.reduce((x, y) -> x + y);
		Stream<String> values2 = Stream.of("a", "bb", "ccc");
		System.out.println(sum.get());
		int result5 = values2.reduce(0, (total, word) -> total + word.length(),
				(total1, total2) -> total1 + total2);
		System.out.println(result5);
		
		Stream<String> values6 = Stream.of("a", "bb", "ccc");
		String[] result6 = values6.toArray(String[]::new);
		System.out.println(result6);
		
		Stream<String> values7 = Stream.of("a", "bb", "ccc", "bb");
		HashSet<String> result7 = values7.collect(HashSet::new, HashSet::add,
				HashSet::addAll);
		System.out.println(result7);
		
		Stream<String> values8 = Stream.of("a", "bb", "ccc", "bb");
		List<String> result8 = values8.collect(Collectors.toList());
		
		Stream<String> values9 = Stream.of("a", "bb", "ccc", "bb");
		TreeSet<String> result9 = values9.collect(Collectors.toCollection(TreeSet::new));
		System.out.println(result9);
		
		
		
		
	}

	public static Stream<Character> characterStream(String s) {
		List<Character> result = new ArrayList<>();
		for (char c : s.toCharArray())
			result.add(c);
		return result.stream();
	}
}
