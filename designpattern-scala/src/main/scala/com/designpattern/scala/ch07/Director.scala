package com.designpattern.scala.ch07

import scala.collection.mutable.ArrayBuffer

/**
 * @author cuckoo03
 */
class Director(_builder: Builder) {
  private val builder = _builder
  def construct(): String = {
    builder.makeTitle("title1")
    builder.makeString("str1")
    builder.makeItems(new ArrayBuffer[String]() += "item1")
    
    return builder.getResult()
  }
}