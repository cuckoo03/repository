package com.designpattern.scala.ch17

/**
 * @author cuckoo03
 */
class IncrementalNumberGenerator(_start: Int, _end: Int, _incrental: Int) 
extends NumberGenerator {
  private val start = _start
  private val end = _end
  private val incremental = _incrental
  private var number = 0
  def execute(): Unit = {
    for { i <- start until end if (i % 5 == 0) } {
      number = i
      notifyObservers()
      number += incremental
    }
  }

  def getNumber(): Int = {
    number
  }
}