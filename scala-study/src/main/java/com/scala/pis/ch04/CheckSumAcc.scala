package com.scala.pis.ch04

class ChecksumAcc {
	private var sum = 0
	def add(b: Byte): Unit = {
		sum += b
	}

	def checksum(): Int = {
		return ~(sum & 0xFF) + 1
	}

	// 등호를 빼먹으면 결과 타입을 잊어버리고 Unint를 반환한다
	def f(): Unit = "a"
	def g() = {
		"a"
	}
}