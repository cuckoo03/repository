package com.designpattern.scala.ch03

/**
 * @author cuckoo03
 */
class CharDisplay(var ch: Char) extends AbstractDisplay {
  override def open(): Unit = {
    Predef.print("<<")
  }
  override def print(): Unit = {
    Predef.print(ch)
  }
  override def close(): Unit = {
    Predef.println(">>")
  }
}