package com.java.inaction.ch08;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.java.inaction.ch06.ToListCollector;

public class Debugging {
	public static void main(String[] args) {
		List<Point> points = Arrays.asList(new Point(12, 2), null);
//		points.stream().map(p -> p.getX()).forEach(System.out::println);
//		points.stream().map(Point::getX).forEach(System.out::println);
		
		List<Integer> numbers = Arrays.asList(1,2,3);
//		numbers.stream().map(Debugging::divideByZero).forEach(System.out::println);
		
		// information logging
		List<Integer> numbers2 = Arrays.asList(2,3,4,5);
		numbers2.stream().map(x -> x + 17).filter(x -> x % 2 == 0).limit(3)
				.forEach(System.out::println);
		
		numbers2.stream()
				.peek(x -> System.out.println("from stream:" + x))
				.map(x -> x + 17)
				.peek(x -> System.out.println("after map:" + x))
				.limit(3)
				.peek(x -> System.out.println("after limit:" + x))
				.collect(Collectors.toList());
	}
	
	public static int divideByZero(int n) {
		return n / 0;
	}
}
