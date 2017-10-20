package com.designpattern.scala.ch05

/**
 * @author cuckoo03
 */
object SingletonMain {
  def main(args: Array[String]) {
    println("start")
    def s = Singleton
    def s2 = Singleton
    if (s == s2) {
      println("same")
    } else {
      println("not same")
    }
  }
}