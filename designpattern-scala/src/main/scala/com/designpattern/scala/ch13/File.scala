package com.designpattern.scala.ch13

/**
 * @author cuckoo03
 */
class File(_name: String, _size: Int) extends Entry {
  private val name = _name
  private val size = _size
  def getName(): String = {
    return name
  }

  def getSize(): Int = {
    return size
  }

  def accept(v: Visitor): Unit = {
    v.visit(this)
  }
}