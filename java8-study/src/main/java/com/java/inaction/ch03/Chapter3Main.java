package com.java.inaction.ch03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import com.java.inaction.ch02.Apple;

public class Chapter3Main {

	public static void process(Runnable r) {
		r.run();
	}

	public static String processFile(BufferedReaderProcessor p)
			throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader("pom.xml"))) {
			return p.process(br);
		}
	}

	public static <T> void forEach(List<T> list, Consumer<T> c) {
		for (T i : list)
			c.accept(i);
	}

	public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
		List<R> result = new ArrayList<>();
		for (T i : list)
			result.add(f.apply(i));

		return result;
	}

	public static List<Apple> map2(List<Integer> list,
			Function<Integer, Apple> f) {
		List<Apple> result = new ArrayList<>();
		for (Integer e: list) {
			result.add(f.apply(e));
		}
		return result;
	}
	
	public static void main(String[] args) {
		process(() -> System.out.println("run"));
		process(() -> {
			System.out.println("run");
		});

		// recurrent pattern
		try {
			String line = processFile((BufferedReader br) -> br.readLine());
			System.out.println(line);
			String twoline = processFile(br -> {
				return br.readLine() + br.readLine();
			});
			System.out.println(twoline);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// consumer interface
		forEach(Arrays.asList(1, 2, 3),
				i -> System.out.println("consumer:" + i));

		// function interface
		List<Integer> result = map(Arrays.asList(1, 2, 3), i -> i * i);
		for (Integer i : result) {
			System.out.println("function:" + i);
		}
		
		Comparator<Apple> c = (a1, a2) -> a1.compareTo(a2);
		
		int portNumber = 1;
		Runnable r = () -> System.out.println(portNumber);
//		portNumber = 2; error
		
		// method reference
		List<String> str = Arrays.asList("a", "b");
		str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
		str.sort(String::compareToIgnoreCase);
		
		Function<String, Integer> stringToInteger = (String s) -> Integer.parseInt(s);
		Function<String, Integer> stringToInteger2 = Integer::parseInt;
		BiPredicate<List<String>, String> contains = (list, e) -> list.contains(e);
		BiPredicate<List<String>, String> contians2 = List::contains;
		
		// constructor reference
		Supplier<Apple> c3 = () -> new Apple();
		c3 = Apple::new;
		Apple a1 = c3.get();
		
		Function<Integer, Apple> c5 = Apple::new;
		c5 = (weight) -> new Apple(weight);
		a1 = c5.apply(10);
		
		List<Apple> apples = map(Arrays.asList(1,2,3,4), Apple::new);

		BiFunction<String, Integer, Apple> c6 = Apple::new;
		c6 = (color, weight) -> new Apple(color, weight); 
		a1 = c6.apply("green", 1);
		
		// concat comparator 
		Comparator<Apple> c7 = Comparator.comparing(Apple::getWeight);
		Comparator<Apple> c8 = Comparator.comparing((Apple a) -> a.getWeight());
		c7.reversed().thenComparing(Apple::getWeight);
		
		// concat predicate
		
		// concat function
		java.util.function.Function<String, String> addHeader = Letter::addHeader;
		java.util.function.Function<String, String> addFooter = Letter::addFooter;
		java.util.function.Function<String, String> pipeline = addHeader.andThen(addFooter);
		pipeline = addHeader.andThen(Letter::addFooter);
	}
}
