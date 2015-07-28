package com.scala.pis.ch04

object Exam01 extends App {
	val check = new ChecksumAcc()
	check.add(3)
	println(check.checksum())
	println(check.f())
	println(check.g())
	
	println(CheckSumAccumulator.cal("1"))
}
