package com.gfpij.ch03

import java.util.function.Function
import java.util.stream.Collectors

import com.fpij.ch03.IterateString
import com.fpij.ch03.Person

import groovy.transform.TypeChecked

@TypeChecked
class Ch03Main {
	static main(args) {
		final String str = "w00t"
		str.chars().forEach { ch ->
			System.out.println(ch)
		}
		str.chars().forEach(System.out.&println)
		str.chars().forEach(IterateString.&printChar)
		str.chars().mapToObj{ ch ->
			Character.valueOf((char) ch)
		}
		.forEach(System.out.&println)
		str.chars().filter(Character.&isDigit)
				.forEach(IterateString.&printChar)
		final List<Person> people = Arrays.asList(new Person("a", 1),
				new Person("b", 3), new Person("c", 2));
		List<Person> ascAge = people.stream().sorted(Person.&ageDiff)
				.collect(Collectors.toList());
		Comparator<Person> compareAsc = { Person p1, Person p2 ->
			p1.ageDiff(p2)
		};
		def compareAsc2 = { Person p1, Person p2 ->
			p1.ageDiff(p2)
		};
		Comparator<Person> compareDesc = compareAsc.reversed();
		System.out.println(people.stream().sorted(compareDesc)
				.collect(Collectors.toList()));
		people.stream().min(Person.&ageDiff).ifPresent(System.out.&println);
		people.stream()
				.sorted{ Person p1, Person p2 ->
					p1.getName().compareTo(p2.getName())
				};
		final Function<Person, String> byName = { Person p -> p.getName() };

		people.stream().sorted(Comparator.comparing(byName));
	}
}
