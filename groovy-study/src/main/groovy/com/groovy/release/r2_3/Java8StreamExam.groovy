package com.groovy.release.r2_3

import groovy.transform.TypeChecked

import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.Function
import java.util.function.Predicate;
import java.util.stream.IntStream

@TypeChecked
class Java8StreamExam {
	static main(args) {
		def upperString = { String it -> it.toUpperCase() } as Function
		
		IntStream.range(1, 100).forEach { println it }
//		Files.lines(Paths.get("")).map{ it.toUpperCase() }.forEach{ println it }
		def intResult = (1..10).stream()
				.filter( { int it -> it % 2 == 0 } as Predicate)
				.map( { int it -> it + "=${it}" } as Function).collect()
		println intResult
		
		def stringResult = ["a", "b", "c"].stream().filter { it == "b" }
		.map (upperString).collect()
		println stringResult
	}
	
}
