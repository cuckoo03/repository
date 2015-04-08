package com.scala.impatient.ch09

import scala.io.Source
import scala.io.BufferedSource
import java.io.PrintWriter
import sys.process._

object Ch09Main extends App {
	val source1: BufferedSource = Source.fromURL("http://google.com", "UTF-8")
	println(source1.toString)
	val source2 = Source.fromString("hello")
	println(source2)
	//	val source3 = Source.stdin
	//	println(source3)

	//read binary file
	val out = new PrintWriter("pom.xml")
	for (i <- 1 to 100) {
		out.println(i)
	}
	
	// process control
	
}
