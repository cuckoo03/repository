package com.designpattern.scala.ch01

/**
 * @author cuckoo03
 */
class Book(n: String) {
  var name = n
  var n1 = n
  def setName():Unit = {
    name = "2"
    n1 = "2"
  }
  
  def getName(): String = {
    return name
  }
  
  def getN1():String = {
    return n1 
  }
}