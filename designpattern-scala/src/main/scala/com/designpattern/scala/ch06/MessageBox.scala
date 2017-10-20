package com.designpattern.scala.ch06

/**
 * @author cuckoo03
 */
class MessageBox(_decochar: Char) extends Product {
  private val decochar = _decochar
  @Override
  override def createClone(): Product = {
    var p = clone().asInstanceOf[Product]
    return p
  }

  @Override
  override def use(s: String): Unit = {
    val length = s.getBytes.length
    for (i <- 0 to length + 4) {
      print(decochar)
    }

    println("")
    println(s"$decochar  $s $decochar")
    for (i <- 0 to length + 4) {
      print(decochar)
    }
    println("")
  }
}