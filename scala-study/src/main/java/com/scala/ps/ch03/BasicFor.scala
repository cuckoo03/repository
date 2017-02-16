package com.scala.ps.ch03

/**
 * @author cuckoo03
 */
object BasicFor {
  def main(args: Array[String]) {
    def list = List("a", "b", "c", "d")

    basicFor1(list)
    guard(list)
    yield1(list)
  }

  def basicFor1(list: List[String]) = {
    for (i <- list)
      println("basicFor1:" + i)
  }

  def guard(list: List[String]) = {
    for (i <- list if (i.contains("c")))
      println("guard:" + i)
  }

  def yield1(list: List[String]) = {
    var sub = for (item <- list if item == "b") yield item
    println("yield:" + sub)

    var sub2 = for { item <- list if item == "c" || item == "d" } yield item
    println("yield:" + sub2)
    // for 내장에 식이 하나만 들어갈 경우  괄호를 여러 식이 들어갈 경우 중괄호를 사용하는 것이 비공식적인 관례
  }
}