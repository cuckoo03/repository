package com.designpattern.scala.ch09

/**
 * @author cuckoo03
 */
abstract class DisplayImpl {
  def rawOpen(): Unit
  def rawPrint(): Unit
  def rawClose(): Unit
}