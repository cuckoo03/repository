package com.java.inaction.ch04;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Ch04Main {
	public static void main(String[] args) {
		List<Dish> menu = Arrays.asList(
				new Dish("pork", false, 800, Dish.Type.MEAT), 
				new Dish("french", true, 700, Dish.Type.OTHER));
		
		List<String> names = menu.stream().filter(d -> d.getCal() > 100)
				.map(Dish::getName).limit(3).collect(Collectors.toList());
		
		names = menu.stream().filter(d -> {
			System.out.println("filtering:" + d.getName());
			return d.getCal() > 100;
		}).map(d -> {
			System.out.println("mapping:" + d.getName());
			return d.getName();
		}).limit(3).collect(Collectors.toList());

		names.forEach(System.out::println);
	}

}
