package com.designpattern.scala.ch03

/**
 * @author cuckoo03
 */
object DisplayMain {
  def main(args:Array[String]) {
    val d1 = new CharDisplay('H')
    val d2 = new StringDisplay("Hello")
    val d3 = new StringDisplay("A")
    
    d1.display()
    d2.display()
    d3.display()
  }
}