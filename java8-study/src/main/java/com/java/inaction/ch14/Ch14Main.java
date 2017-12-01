package com.java.inaction.ch14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Ch14Main {

	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<>();
		map.put("a", 1);
		map.put("c", null);
		Integer count = 0;
		if (map.containsKey("a")) {
			count = map.get("a");
		}
		System.out.println(map);
		Integer b = map.getOrDefault("c", 0);
		System.out.println(b);

		map.forEach((String k, Integer v) -> System.out.println(k + "," + v));

		ConcurrentHashMap<String, Integer> map2 = new ConcurrentHashMap<>();
		map2.put("a", 1);
		map2.put("b", 2);
		Optional<Integer> maxValue = Optional.of(map2.reduceValues(1,
				Integer::max));
		System.out.println(maxValue);

		int[] evenNumbers = new int[10];
		Arrays.setAll(evenNumbers, i -> i * 2);
		System.out.println(evenNumbers[9]);
		
		final String dir = System.getProperty("user.dir");
		try (Stream<String> files = Files.lines(Paths.get(dir + "\\pom.xml"))) {
			files.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
