package com.designpattern.scala.ch09

/**
 * @author cuckoo03
 */
object BridgeMain {
  def main(args:Array[String]) {
    val d1 = new Display(new StringDisplayImpl("A"))
    val d2 = new CountDisplay(new StringDisplayImpl("B"))
    val d3:CountDisplay = new CountDisplay(new StringDisplayImpl("C"))
    
    d1.display()
    d2.display()
    d3.display()
    d3.multiDisplay(3)
  }
}