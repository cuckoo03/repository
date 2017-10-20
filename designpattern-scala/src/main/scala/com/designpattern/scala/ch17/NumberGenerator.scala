package com.designpattern.scala.ch17

import scala.collection.mutable.ListBuffer

/**
 * @author cuckoo03
 */
abstract class NumberGenerator {
  private val observers = new ListBuffer[Observer]()

  def addObserver(observer:Observer):Unit = observers += observer
  def deleteObserver(observer:Observer):Unit = observers += observer
  def notifyObservers():Unit = {
    val it = observers.iterator
    while (it.hasNext) {
      val o = it.next()
      o.update(this)
    }
  }
  def getNumber():Int
  def execute():Unit
}