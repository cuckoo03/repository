package com.designpattern.scala.ch07

import scala.collection.mutable.ArrayBuffer


/**
 * @author cuckoo03
 */
trait Builder {
  def makeTitle(title:String)
  def makeString(str:String)
  def makeItems(items:ArrayBuffer[String])
  def getResult():String
}