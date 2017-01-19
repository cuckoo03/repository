package com.java.impatient.ch02;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		unique.forEach(System.out::println);
	}

	public static Stream<Character> characterStream(String s) {
		List<Character> result = new ArrayList<>();
		for (char c : s.toCharArray())
			result.add(c);
		return result.stream();
	}
}
