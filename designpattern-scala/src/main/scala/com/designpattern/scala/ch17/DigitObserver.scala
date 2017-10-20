package com.designpattern.scala.ch17

/**
 * @author cuckoo03
 */
class DigitObserver extends Observer {
  def update(generator: NumberGenerator): Unit = {
    println("digitObserver:" + generator.getNumber())
    Thread.sleep(100)
  }
}