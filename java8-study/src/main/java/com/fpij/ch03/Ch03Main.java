package com.fpij.ch03;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.gfpij.ch04.Asset;

public class Ch03Main {
	public static void main(String[] args) {
		final String str = "w00t";
		str.chars().forEach(ch -> System.out.println(ch));
		str.chars().forEach(System.out::println);
		str.chars().forEach(IterateString::printChar);
		str.chars().mapToObj(ch -> Character.valueOf((char) ch))
				.forEach(System.out::println);
		str.chars().filter(Character::isDigit)
				.forEach(IterateString::printChar);

		final List<Person> people = Arrays.asList(new Person("a", 1),
				new Person("b", 3), new Person("c", 2));
		List<Person> ascAge = people.stream().sorted(Person::ageDiff)
				.collect(Collectors.toList());
		Comparator<Person> compareAsc = (p1, p2) -> p1.ageDiff(p2);
		System.out.println(ascAge);
		Comparator<Person> compareDesc = compareAsc.reversed();
		System.out.println(people.stream().sorted(compareDesc)
				.collect(Collectors.toList()));
		people.stream().min(Person::ageDiff).ifPresent(System.out::println);
		people.stream()
				.sorted((p1, p2) -> p1.getName().compareTo(p2.getName()));
		final Function<Person, String> byName = p -> p.getName();

		people.stream().sorted(Comparator.comparing(byName));
		
		new File(".").listFiles(File::isHidden);
	}
}
