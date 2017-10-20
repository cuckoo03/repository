package com.designpattern.scala.ch19

/**
 * @author TC
 */
object SafeFrameMain {
  def main(args: Array[String]) {
    val frame = new SafeFrame("state sample")
    while (true) {
      for (hour <- 0 to 24) {
        frame.setClock(hour)
        try {
          Thread.sleep(1000)
        } catch {
          case ie: InterruptedException =>
        }
      }
    }
  }
}