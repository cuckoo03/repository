package com.designpattern.scala.ch11

/**
 * @author cuckoo03
 */
class File(val _name: String, val _size: Int) extends Entry {
  private val name = _name
  private val size = _size

  def getName(): String = {
    return name
  }

  def getSize(): Int = {
    return size
  }

  @Override
  override def printList(prefix: String): Unit =
    println(prefix + "/" + this)
}