package com.java.inaction.ch05;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.java.inaction.ch04.Dish;

public class Ch05Main {
	public static void main(String[] args) {
		List<Dish> menu = Arrays.asList(
				new Dish("pork", false, 800, Dish.Type.MEAT), 
				new Dish("french", true, 700, Dish.Type.OTHER));
		
		// filtering predicate
		List<Dish> ve = menu.stream().filter(Dish::isVe)
				.collect(Collectors.toList());
		ve.forEach(System.out::println);
		
		// filtering distinct
		List<Integer> numbers = Arrays.asList(1,1,2); 
		numbers.stream().distinct().forEach(System.out::println);
		
		// limit
		
		// skip
		
		numbers.stream().skip(1).forEach(System.out::println);
		
		// filtering exam
		menu.stream().filter(d -> d.getType() == Dish.Type.OTHER).limit(1)
				.collect(Collectors.toList()).forEach(System.out::println);
		
		// mapping
		menu.stream().map(Dish::getName).map(String::length)
				.forEach(System.out::println);
		
		// flatten stream
		String[] arrayOfWords = {"goodbye", "world"};
		Arrays.stream(arrayOfWords).map(w -> w.split("")).map(Arrays::stream)
				.forEach(System.out::println);
		Arrays.stream(arrayOfWords).map(w -> w.split(""))
				.flatMap(Arrays::stream).forEach(System.out::println);
		
		// searching and matching
		if (menu.stream().anyMatch(Dish::isVe))
			System.out.println("anyMatch");
		
		// allMatch
		// noneMatch
		
		//searching element
		Optional<Dish> findAny = menu.stream().filter(Dish::isVe).findAny();
		System.out.println(findAny);
		menu.stream().filter(Dish::isVe).findAny()
				.ifPresent(d -> System.out.println(d.getName()));
		Optional<Dish> findFirst = menu.stream().findFirst();
		System.out.println(findFirst);
		
		// reducing
		int count = Arrays.asList(1, 2, 3).stream().reduce(1, (a, b) -> a + b);
		Arrays.asList(1, 2, 3).stream().reduce(1, Integer::sum);
		Arrays.asList(1, 2, 3).stream().reduce(1, (a, b) -> a + b);
		System.out.println(count);
		
		// count map reduce
		count = Arrays.asList(1, 2, 3).parallelStream().map(d -> 1).reduce(0, Integer::sum);
		System.out.println(count);
		
		// exam
		Trader t1 = new Trader("trader1", "city1");
		Trader t2 = new Trader("trader2", "city2");
		Trader t3 = new Trader("trader3", "city1");
		Trader t4 = new Trader("trader4", "city2");
		
		List<Transaction> transactions = Arrays.asList(
				new Transaction(t1, 2011, 300),
				new Transaction(t2, 2012, 400),
				new Transaction(t1, 2013, 500),
				new Transaction(t3, 2013, 600),
				new Transaction(t4, 2013, 700)
				);

		// find year 2011
		transactions.stream().filter(t -> t.getY() == 2012)
				.sorted(Comparator.comparing(Transaction::getV))
				.forEach(System.out::println);

		// distict city
		transactions.stream().map(t -> t.getT().getC()).distinct()
				.forEach(System.out::println);

		// find trader by city2 order by name
		transactions
				.stream()
				.filter(t -> t.getT().getC() == "city2")
				.sorted(Comparator
						.comparing((Transaction t) -> t.getT().getC()))
				.forEach(System.out::println);

		// distinct trader order by name
		String allName = transactions.stream().map(t -> t.getT().getN())
				.distinct().sorted().reduce("", (n1, n2) -> n1 + n2 + ",");
		allName = transactions.stream().map(t -> t.getT().getN()).distinct().sorted()
				.collect(Collectors.joining(", "));
		System.out.println(allName);

		// is exist city2 trader
		boolean existTrader = transactions.stream()
				.anyMatch(t -> t.getT().getC() == "city2");
		System.out.println(existTrader);

		// print transaction value in city1
		transactions.stream().filter(t -> t.getT().getC() == "city2")
				.map(Transaction::getV).forEach(System.out::println);

		// max transaction value
		Optional<Transaction> max = transactions.stream().max(
				Comparator.comparing(Transaction::getV));
		System.out.println("max " + max);

		// min transaction value
		Optional<Transaction> min = transactions.stream().min(
				Comparator.comparing(Transaction::getV));
		System.out.println("min " + min);
		// or
		min = transactions.stream().reduce(
				(it1, it2) -> it1.getV() < it2.getV() ? it1 : it2);
		System.out.println("min " + min);
		
		// numeric stream
		IntStream intSteam = menu.stream().mapToInt(Dish::getCal);
		Stream<Integer> stream = intSteam.boxed();
		
		// optionalInt
		OptionalInt maxCal = menu.stream().mapToInt(Dish::getCal).max();
		System.out.println(maxCal.orElse(1));
		
		// number range
		IntStream evenNum = IntStream.rangeClosed(1, 100).filter(
				n -> n % 2 == 0);
		System.out.println(evenNum.count());
		
		// exam
		Stream<int[]> result = IntStream
				.rangeClosed(1, 100)
				.boxed()
				.flatMap(
						a -> IntStream
								.rangeClosed(a, 100)
								.filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
								.mapToObj(
										b -> new int[] { a, b,
												(int) Math.sqrt(a * a + b * b) }));
		result.limit(5).forEach(
				t -> System.out.println(t[0] + " " + t[1] + " " + t[2]));
		
		// make stream
		Stream.of("a", "b", "c").map(String::toUpperCase)
				.forEach(System.out::println);
		
		// make stream from file
		
		// infinite stream
		Stream.iterate(0, n -> n + 2).limit(10).forEach(System.out::println);
		
		// generate
		Stream.generate(Math::random).limit(5).forEach(System.out::println);
	}
}
