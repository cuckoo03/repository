package com.scala.impatient.ch01

import scala.math.BigInt
import scala.math._
import scala.util.Random

object Ch01Main extends App {
  println("test")
  println(1.toString())
  println(1.to(10))
  println("Hello".intersect("World"))

  val answer = 8 * 5 + 2
  var a = 1
  var b = 1
  println (a.+(b))
  1 to 10
  a+=1
  println(a)
  println(BigInt.probablePrime(100, Random))
  println ("Hello".distinct)
  println ("Hello"(4))
  println ("Hello".apply(4))
  println (BigInt("1") + BigInt("2"))
  println (sqrt(2))
  println ("A".count(_.isUpper))
  println ("Harry".patch(1, "ung", 2))
  println (10 max 3)
  
  //ex.8
  println (BigInt(Random.nextInt(10000)).hashCode)

  //ex.9
  assert ('A'.equals("ABC".head))
  assert ('C' == "ABC".last)
  
  //ex 10
  val str = "ABC"
  assert("A" == str.take(1))
  assert("AB" == str.take(2))
  assert ("BC" == str.drop(1))
  assert ("C" == str.drop(2))
  assert ("C" == str.takeRight(1))
  assert ("BC" == str.takeRight(2))
  assert ("AB" == str.dropRight(1))
  assert ("A" == str.dropRight(2))
  
}