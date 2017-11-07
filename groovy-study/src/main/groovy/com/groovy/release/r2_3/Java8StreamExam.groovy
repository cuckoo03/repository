package com.groovy.release.r2_3

import groovy.transform.TypeChecked

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.IntStream

@TypeChecked
class Java8StreamExam {
	static main(args) {
		IntStream.range(1, 100).forEach { println it }
		Files.lines(Paths.get("")).map{ it.toUpperCase()}.forEach{ println it }
	}
}
