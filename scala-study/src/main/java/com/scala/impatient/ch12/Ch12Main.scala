package com.scala.impatient.ch12
import scala.math._
object Ch12Main extends App {
	val num = 3.14
	val fun = ceil _
	println(fun(num))

	// closer
	def mulby(factor: Double) = (x: Double) => factor * x
	val f = mulby(3)
}