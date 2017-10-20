package com.designpattern.scala.ch09

/**
 * @author cuckoo03
 */
class Display(val _impl: DisplayImpl) {
  private val impl = _impl
  def open(): Unit = impl.rawOpen()
  def print(): Unit = impl.rawPrint()
  def close(): Unit = impl.rawClose()
  final def display():Unit = {
    open()
    print()
    close()
  }
}