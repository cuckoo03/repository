package com.java.inaction.ch06;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.java.inaction.ch04.Dish;
import com.java.inaction.ch04.Dish.Type;

public class Ch06Main {
	public static <T> Collector<T, ?, Long> counting() {
		return Collectors.reducing(0L, e -> 1L, Long::sum);
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
		
		double avg = menu.stream().collect(Collectors.averagingInt(Dish::getCal));
		System.out.println(avg);
		
		IntSummaryStatistics intStat = menu.stream().collect(
				Collectors.summarizingInt(Dish::getCal));
		System.out.println(intStat);
		LongSummaryStatistics longStat = menu.stream().collect(
				Collectors.summarizingLong(Dish::getCal));
		System.out.println(longStat);
		
		// concat string
		menu.stream().map(Dish::getN).collect(Collectors.joining(", "));
		
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
		//or
		total = menu.stream().map(Dish::getCal).reduce(Integer::sum).get();
		//or
		total = menu.stream().mapToInt(Dish::getCal).sum();
		
		String join = menu.stream().map(Dish::getN).collect(Collectors.joining());
		join = menu.stream().map(Dish::getN)
				.collect(Collectors.reducing("", (s1, s2) -> s1 + s2));
		//or
		join = menu.stream().map(Dish::getN)
				.collect(Collectors.reducing((s1, s2) -> s1 + s2)).get();
		//or
		join = menu.stream().collect(
				Collectors.reducing("", Dish::getN, (s1, s2) -> s1 + s2));
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
		
		
		
		
	}
	
	public enum CalLevel {
		DIET, NORMAL, FAT
	}
}
