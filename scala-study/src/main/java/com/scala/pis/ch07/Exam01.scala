package com.scala.pis.ch07

import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import scala.util.control.Breaks

object Exam01 {
	def main(args: Array[String]) {
		val filename = if (!args.isEmpty) args(0) else "default"
		println(filename)

		println("")

		// travel collection
		val filesHere = (new java.io.File("./")).listFiles()
		for (file <- filesHere)
			println(file)

		println("")

		for (i <- 0 to 3)
			println(i)

		println("")

		//filtering
		for (file <- filesHere if file.isFile() if file.getName.endsWith(".scala")) {
			println("filter:" + file)
		}

		println("")

		// nested loop
		def fileLines(file: java.io.File) =
			scala.io.Source.fromFile(file).getLines().toList

		def grep(pattern: String) =
			for {
				file <- filesHere if file.getName.endsWith(".scala");
				line <- fileLines(file) if line.trim.matches(pattern)
			} println("nested loop:" + line)
		grep(".*gcd.*")

		println("")

		def scalaFiles =
			for (
				file <- filesHere if file.getName.endsWith(".scala")
			) yield file
		println(scalaFiles.toList)

		println("")

		//Array[File] to Array[Int]
		def forLineLengths =
			for {
				file <- filesHere
				if file.getName.endsWith(".scala")
				line <- fileLines(file)
				trimmed = line.trim
				if trimmed.matches(".*for.*")
			} yield trimmed.length()

		for (i <- forLineLengths) println(i)

		// try catch
		val n = 2
		var half =
			if (n % 2 == 0)
				n / 2
			else
				throw new RuntimeException("n must be even")

		try {
			val f = new FileReader("input.txt")
		} catch {
			case e: FileNotFoundException => println(e.getMessage)
			case e: IOException => e.printStackTrace()
		} finally {
			println("finally")
		}

		def f(): Int = try { return 1 } finally { return 2 }
		// 스칼라는 try절에서 값을 명시적으로 리턴하지 않고 exception이 없으면 finally절이 호출되지 않는다.
		def g(): Int = try { 1 } finally { 2 }
		println(f())
		println(g())

		// match expression
		val firstArg = "C"
		firstArg match {
			// 모든 케이스마다 암묵적으로 break문이 있다
			case "A" => println("A")
			case "B" => println("B")
			case _ => println("_")
		}

		val friend = firstArg match {
			case "A" => "A"
			case _ => "_"
		}
		println(friend)

		// break
		var i = 0
		var foundIt = false
		while (i < filesHere.length && !foundIt) {
			if (!filesHere(i).getName.startsWith("-")) {
				if (!filesHere(i).getName.endsWith(".scala"))
					foundIt = true
			}
			i = i + 1
		}

		//breakable
		val in = "A"
		Breaks.breakable(while (true) {
			println("?")
			if (in == "A") Breaks.break()
		})

		def makeRowSeq(row: Int) =
			for (col <- 1 to 10) yield {
				val prod = (row * col).toString()
				val padding = " " * (4 - prod.length())
				padding + prod
			}

		// 하나의 열을 문자열로 변환
		def makeRow(row: Int) = makeRowSeq(row).mkString(":")

		def multiTable() = {
			val tableSeq = for (row <- 1 to 5)
				yield makeRow(row)
			tableSeq.mkString("\n")
		}

		println(multiTable())

		//how to use yield
		val ts: IndexedSeq[Int] = for (row <- 1 to 5)
			yield row
		println(ts)
	}
}