package com.designpattern.scala.ch17

/**
 * @author cuckoo03
 */
class GraphObserver extends Observer {
  def update(generator: NumberGenerator): Unit = {
    print("graphObserver:")
    val count = generator.getNumber()
    for (i <- 0 until count) {
      print("*")
    }
    println("")
    Thread.sleep(100)
  }
}