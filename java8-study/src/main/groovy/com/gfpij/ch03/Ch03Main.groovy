package com.gfpij.ch03

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.Stream

import com.fpij.ch03.IterateString
import com.fpij.ch03.Person

import groovy.transform.TypeChecked

@TypeChecked
class Ch03Main {
	static main(args) {
		final def str = "w00t"
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

		final def people = Arrays.asList(new Person("a", 1),
				new Person("b", 3), new Person("c", 2))
		def compareAsc = { Person p1, Person p2 -> p1.ageDiff(p2) } as Comparator<Person>
		def compareAsc2 = { Person p1, Person p2 -> p1.ageDiff(p2) } 

		// error
		 def ascAge = people.stream().sorted(compareAsc).collect(Collectors.toList())
		people.stream().sorted { Person p1, Person p2 ->
			p1.getName().compareTo(p2.getName())
		}
		final def byName = { Person p -> p.getName() } as Function<Person, String>
		people.stream().min(Person.&ageDiff).ifPresent(System.out.&println)
		people.stream().sorted(Comparator.comparing(byName))

		def list = ArrayList.&new as List
		people.stream().collect(
			ArrayList.&new, ArrayList.&add, ArrayList.&addAll) as List<Person>
		def map = people.stream().collect(Collectors.groupingBy(Person.&getAge))
		println map
		
		def map2 = people.stream().collect(Collectors.groupingBy(
			Person.&getAge, Collectors.mapping(Person.&getName, Collectors.toList())))
		println map2
		
		def byAge = Comparator.comparing(Person.&getAge)
		def map3 = people.stream().collect(Collectors.groupingBy(
			{ Person p -> p.getName().charAt(0) } as Function, 
			Collectors.reducing(BinaryOperator.maxBy(byAge))))
		println map3
		
		Files.list(Paths.get(".")).filter(Files.&isDirectory)
			.forEach(System.out.&println)
		def files = new File(".settings").list(
			{ d, n -> return n.endsWith(".prefs") }
		)
		println files
		
		Files.newDirectoryStream(
			Paths.get(".settings"), { path -> path.toString().endsWith(".prefs") }
		).forEach(System.out.&println)
		def files2 = new File(".").listFiles({ File f-> f.isHidden() } as FileFilter)
		println files2

		// groovy에서는 메서드레퍼런스가 모호해서 컴파일 에러 발생
//		new File(".").listFiles(File.&isHidden)
		
		showFlatmap()
		
		def path = Paths.get(".")
		def watchService = path.getFileSystem().newWatchService()
		path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)
//		watchService.poll().pollEvents().stream().forEach({ e -> e.context()})
		
		
	}
	static void showFlatmap() {
		def files = Stream.of(new File(".").listFiles())
			.flatMap({ f -> f.listFiles() == null ?
				Stream.of(f) : Stream.of(f.listFiles())
			}).collect(Collectors.toList())
		println files.size()
	}
}
