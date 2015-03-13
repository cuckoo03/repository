package com.scala.impatient.ch04

import java.util.TreeMap

import scala.collection.SortedMap
import scala.collection.mutable.HashMap
import scala.collection.mutable.Map
import scala.collection.JavaConversions.mapAsScalaMap

object Ch04Main extends App {
  var scores = Map("a" -> 1, "b" -> 2)
  println(scores)
  var scores2 = new HashMap[String, Int]
  println(scores2)
  var scores3 = Map(("a", 1), ("b", 2))
  println(scores3)
  println(Map)
  println(HashMap)
  println(scores("a"))
  println(scores.contains("a"))
  println(scores.contains("z"))
  println(scores.getOrElse("z", 0))
  scores("a") = 11
  println(scores("a"))
  scores += ("c" -> 3, "d" -> 4)
  println(scores)
  scores -= "d"
  println(scores)

  // map iterate 
  for (v <- scores.values) {
    println(v)
  }

  for ((k, v) <- scores) yield {
    println(v, k)
  }

  // map sort
  var scoresSort = SortedMap("b" -> 2, "a" -> 1)
  println(scoresSort)
  scoresSort += ("c" -> 3)
  println(scoresSort)

  // interation java
  var score4: scala.collection.mutable.Map[String, Int] = new java.util.TreeMap[String, Int]
  println(score4)

  // tuple
  val t = (1, 2.1, "a")
  var (first, second, third) = t
  println(t)

  // zipping
  var symbols = Array("<", ">")
  val counts = Array(2, 2)
  var pairs = symbols.zip(counts)
  for ((s, n) <- pairs) {
    Console.println(s + "," + n)
  }

}