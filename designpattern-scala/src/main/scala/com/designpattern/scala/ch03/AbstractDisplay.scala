package com.designpattern.scala.ch03

/**
 * @author cuckoo03
 */
abstract class AbstractDisplay {
  def open():Unit
  def print():Unit
  def close():Unit
  final def display():Unit = {
    open()
    for (i <- 0 until 5) {
      print()
    }
    close()
  }
  
}