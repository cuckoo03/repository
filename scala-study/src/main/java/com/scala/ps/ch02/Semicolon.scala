package com.scala.ps.ch02

/**
 * @author cuckoo03
 */
object Semicolon {
  def main(args: Array[String]) {
    a("a")
    b("b")
  }
  def a(s: String) = println(s)
  def b(s: String) = {
    println(s)
  }
}