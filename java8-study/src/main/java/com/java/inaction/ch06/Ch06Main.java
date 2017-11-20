package com.java.inaction.ch06;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.java.inaction.ch04.Dish;
import com.java.inaction.ch04.Dish.Type;

public class Ch06Main {
	public static <T> Collector<T, ?, Long> counting() {
		return Collectors.reducing(0L, e -> 1L, Long::sum);
	}

	public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
		return IntStream.rangeClosed(2, n).boxed()
				.collect(partitioningBy(c -> isPrime(c)));
	}

	public static boolean isPrime(int c) {
		int cRoot = (int) Math.sqrt((double) c);
		return IntStream.rangeClosed(2, cRoot).noneMatch(i -> c % i == 0);
	}

	public static boolean isPrime(List<Integer> primes, int c) {
		return primes.stream().noneMatch(i -> c % i == 0);
	}

	public static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
		int i = 0;
		for (A item : list) {
			if (!p.test(item)) {
				return list.subList(0, i);
			}
			i++;
		}
		return list;
	}

	public static void main(String[] args) {
		List<Dish> menu = Arrays
				.asList(new Dish("pork", false, 800, Dish.Type.MEAT), new Dish(
						"french", true, 700, Dish.Type.OTHER));
		long count = menu.stream().collect(Collectors.counting());
		System.out.println(count);
		count = menu.stream().count();

		// find min max from stream
		Comparator<Dish> dishCalComparator = Comparator.comparing(Dish::getCal);
		Optional<Dish> most = menu.stream().collect(
				Collectors.maxBy(dishCalComparator));
		System.out.println(most);

		// summarization
		int total = menu.stream().collect(Collectors.summingInt(Dish::getCal));
		System.out.println(total);
		long longTotal = menu.stream().collect(
				Collectors.summingLong(Dish::getCal));
		System.out.println(longTotal);

		double avg = menu.stream().collect(
				Collectors.averagingInt(Dish::getCal));
		System.out.println(avg);

		IntSummaryStatistics intStat = menu.stream().collect(
				Collectors.summarizingInt(Dish::getCal));
		System.out.println(intStat);
		LongSummaryStatistics longStat = menu.stream().collect(
				Collectors.summarizingLong(Dish::getCal));
		System.out.println(longStat);

		// concat string
		menu.stream().map(Dish::getName).collect(Collectors.joining(", "));

		// reducing
		total = menu.stream().collect(
				Collectors.reducing(0, Dish::getCal, (i, j) -> i + j));
		Optional<Dish> mostCal = menu.stream().collect(
				Collectors.reducing((d1, d2) -> d1.getCal() > d2.getCal() ? d1
						: d2));
		System.out.println(mostCal);

		Arrays.asList(1, 2, 3)
				.stream()
				.reduce(new ArrayList<Integer>(),
						(List<Integer> l, Integer e) -> {
							l.add(e);
							return l;
						}, (List<Integer> l1, List<Integer> l2) -> {
							l1.addAll(l2);
							return l1;
						});
		total = menu.stream().collect(
				Collectors.reducing(0, Dish::getCal, Integer::sum));
		// or
		total = menu.stream().map(Dish::getCal).reduce(Integer::sum).get();
		// or
		total = menu.stream().mapToInt(Dish::getCal).sum();

		String join = menu.stream().map(Dish::getName)
				.collect(Collectors.joining());
		join = menu.stream().map(Dish::getName)
				.collect(Collectors.reducing("", (s1, s2) -> s1 + s2));
		// or
		join = menu.stream().map(Dish::getName)
				.collect(Collectors.reducing((s1, s2) -> s1 + s2)).get();
		// or
		join = menu.stream().collect(
				Collectors.reducing("", Dish::getName, (s1, s2) -> s1 + s2));
		System.out.println(join);

		// grouping
		Map<Dish.Type, List<Dish>> dish = menu.stream().collect(
				Collectors.groupingBy(Dish::getType));
		System.out.println(dish);

		Map<CalLevel, List<Dish>> dishByCal = menu.stream().collect(
				Collectors.groupingBy(d -> {
					if (d.getCal() <= 400)
						return CalLevel.DIET;
					else
						return CalLevel.NORMAL;
				}));
		System.out.println(dishByCal);

		// Nlevel grouping
		Map<Dish.Type, Map<CalLevel, List<Dish>>> dishByType = menu.stream()
				.collect(
						Collectors.groupingBy(Dish::getType,
								Collectors.groupingBy((Dish d) -> {
									if (d.getCal() <= 400)
										return CalLevel.DIET;
									else
										return CalLevel.NORMAL;
								})));
		System.out.println(dishByType);

		Map<Type, Long> typesCount = menu.stream().collect(
				groupingBy(Dish::getType, counting()));
		System.out.println(typesCount);

		Map<Type, Optional<Dish>> mostCalByType = menu.stream().collect(
				groupingBy(Dish::getType, maxBy(comparing(Dish::getCal))));
		System.out.println(mostCalByType);

		Map<Type, Dish> mostCalByType2 = menu.stream().collect(
				groupingBy(
						Dish::getType,
						collectingAndThen(maxBy(comparing(Dish::getCal)),
								Optional::get)));
		System.out.println(mostCalByType2);

		Map<Type, Integer> totalCalByType = menu.stream().collect(
				groupingBy(Dish::getType, summingInt(Dish::getCal)));
		System.out.println(totalCalByType);

		Map<Type, Set<CalLevel>> calLevelByType = menu.stream().collect(
				groupingBy(Dish::getType, mapping(d -> {
					if (d.getCal() <= 400)
						return CalLevel.DIET;
					else
						return CalLevel.NORMAL;
				}, toSet())));
		System.out.println(calLevelByType);

		calLevelByType = menu.stream().collect(
				groupingBy(Dish::getType, mapping(d -> {
					if (d.getCal() <= 400)
						return CalLevel.DIET;
					else
						return CalLevel.DIET;
				}, toCollection(HashSet::new))));
		System.out.println(calLevelByType);

		// partitionaing
		Map<Boolean, List<Dish>> partitionMenu = menu.stream().collect(
				partitioningBy(Dish::isVe));
		System.out.println(partitionMenu);

		Map<Boolean, Map<Type, List<Dish>>> dishedByType = menu.stream()
				.collect(partitioningBy(Dish::isVe, groupingBy(Dish::getType)));
		System.out.println(dishedByType);

		Map<Boolean, Dish> mostCalPartitionByVe = menu.stream().collect(
				partitioningBy(
						Dish::isVe,
						collectingAndThen(maxBy(comparingInt(Dish::getCal)),
								Optional::get)));
		System.out.println(mostCalPartitionByVe);

		// collector interface
		// characteristics method
		List<Dish> dishes = menu.stream().collect(new ToListCollector<Dish>());
		dishes = menu.stream().collect(toList());
		System.out.println(dishes);

		// menu.stream().collect(ArrayList::new, List::add, List::addAll);
		// eclipse 4.4 bug로 인해 에러 발생
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=457791
		
		
	}

	public enum CalLevel {
		DIET, NORMAL, FAT
	}
}
