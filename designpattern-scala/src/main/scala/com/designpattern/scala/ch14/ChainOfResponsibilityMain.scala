package com.designpattern.scala.ch14

/**
 * @author cuckoo03
 */
object CoRMain {
  def main(args: Array[String]) {
    val a = new NoSupport("Alice")
    val b = new LimitSupport("Bob", 1)
    val c = new SpecialSupport("Charlie", 2)

    a.setNext(b).setNext(c)
    for (i <- 0 until 5) {
      a.support(new Trouble(i))
    }
  }
}