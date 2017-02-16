package com.scala.pis.ch06

class Rational(n: Int, d: Int) {
	// main constructor
	require(d != 0)

	private var g = gcd(n.abs, d.abs)

	val numer: Int = n / g
	val denom: Int = d / g

	def this(n: Int) = this(n, 1) // auxiliary constructor
	// 스칼라 클래스에서는 주 생성자만이 슈퍼클래스의 생성자를 호출할 수 잇다

	def Rational(n: Int) = {
		// another constructor
	}

	def add(that: Rational): Rational =
		new Rational(numer * that.denom + that.numer * denom, denom * that.denom)

	// method overloading
	def +(that: Rational): Rational =
		new Rational(numer * that.denom + that.numer * denom, denom * that.denom)
	
	def +(i: Int): Rational =
		new Rational(numer + i * denom, denom)

	def lessThan(that: Rational) = {
		this.numer * that.denom < that.numer * this.denom
	}

	def max(that: Rational) =
		if (lessThan(that))
			that
		else
			this

	private def gcd(a: Int, b: Int): Int =
		if (b == 0) a else gcd(b, a % b)

	@Override
	override def toString = numer + "/" + denom
}