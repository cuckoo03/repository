package com.designpattern.scala.ch09

/**
 * @author cuckoo03
 */
class CountDisplay(var impl: DisplayImpl) extends Display(impl) {
  def multiDisplay(times: Int) = {
    open()
    for (i <- 0 until times) {
      print()
    }
    close()
  }

}