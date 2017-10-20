package com.designpattern.scala.ch11

/**
 * @author cuckoo03
 */
abstract class Entry {
  def getName(): String
  def getSize(): Int

  @throws(classOf[FileTreatmentException])
  def add(entry: Entry): Entry = {
    throw new FileTreatmentException()
  }

  def printList(): Unit = {
    printList("")
  }

  @throws(classOf[FileTreatmentException])
  def printList(prefix: String): Unit = {
    throw new FileTreatmentException("")
  }

  @Override
  override def toString(): String = return getName() + "(" + getSize() + ")"

  def getFullName(): String = {
    val fullName: StringBuilder = new StringBuilder()
    var entry = this
    do {
      fullName.insert(0, "/" + entry.getName())
    } while (entry != null)

    return fullName.toString()
  }
}