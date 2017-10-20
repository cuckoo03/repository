package com.designpattern.scala.ch06

/**
 * @author cuckoo03
 */
class UnderlinePen(var _ulchar: Char) extends Product {
  private val ulchar = _ulchar

  @Override
  override def createClone(): Product = {
    var p = clone().asInstanceOf[Product]
    return p
  }
  
  @Override
  override def use(s: String): Unit = {
    val length = s.getBytes.length
    println(s"\\$s\\")
    print(" ")
    for (i <- 0 to length) {
      print(ulchar)
    }
    println(" ")
  }
}