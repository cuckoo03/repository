package com.scala.pis.ch06

class Rational(n: Int, d: Int) {
	require(d != 0)

	private var g = gcd(n.abs, d.abs)

	val numer: Int = n / g
	val denom: Int = d / g

	def this(n: Int) = this(n, 1) // constructor

	def add(that: Rational): Rational =
		new Rational(numer * that.denom + that.numer * denom, denom * that.denom)

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

	override def toString = numer + "/" + denom
}