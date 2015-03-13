package com.scala.impatient.ch03

import scala.collection.mutable.ArrayBuffer
import scala.Array

object Ch03Main extends App {
  val nums = new Array[Int](10)
  println(nums(0))
  val a = new Array[String](20)
  println(a(0))
  val s = Array("H", "W")
  println(s)
  s(0) = "A"
  println(s(0))

  val b = ArrayBuffer[Int]()
  b += 1
  println(b)
  b += (2, 3)
  println(b)
  b.trimEnd(1)
  println(b)
  println(b.toArray)

  for (i <- 0 until (b.length)) {
    println(b(i))
  }

  println(0 until 5)
  println(0.until(5))
  
  println(Array(1,2).sum)
  println(ArrayBuffer("B", "A").max)
  
}