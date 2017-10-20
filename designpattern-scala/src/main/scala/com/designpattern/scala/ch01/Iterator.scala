package com.designpattern.scala.ch01

/**
 * @author cuckoo03
 */
trait Iterator {
  def hasNext(): Boolean
  def next(): Book
}