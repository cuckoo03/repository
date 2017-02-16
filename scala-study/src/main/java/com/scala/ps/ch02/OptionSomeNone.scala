package com.scala.ps.ch02

/**
 * @author cuckoo03
 */
object OptionNull {
  def main(args: Array[String]) {
    var map = Map("a" -> "1", "b" -> "2", "c" -> "3")

    println("a:" + map.get("a"))
    println("b:" + map.get("b"))
    println("none:" + map.get("d"))

    println("a:" + map.get("a").get)
    println("else b:" + map.get("b").getOrElse("else b"))
    println("none d:" + map.get("d").getOrElse("none d"))

    println("aa:" + map.get("aa").get)
  }
}