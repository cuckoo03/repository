package com.designpattern.scala.ch13

/**
 * @author cuckoo03
 */
abstract class Entry extends Acceptor {
  def getName(): String
  def getSize(): Int
  @throws(classOf[FileTreatmentException])
  def add(entry: Entry): Entry = {
    throw new FileTreatmentException()
  }
  @throws(classOf[FileTreatmentException])
  def iterator(): Iterator[Entry] = {
    throw new FileTreatmentException()
  }
  override def toString() = getName() + "(" + getSize() + ")"
}