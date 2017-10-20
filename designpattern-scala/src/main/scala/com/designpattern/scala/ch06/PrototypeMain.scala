package com.designpattern.scala.ch06

/**
 * @author cuckoo03
 */
object PrototypeMain {
  def main(args:Array[String]) {
    val manager = new Manager
    val upen = new UnderlinePen('~')
    val mbox = new MessageBox('*')
    val sbox = new MessageBox('/')
    manager.register("strong", upen)
    manager.register("warning", mbox)
    manager.register("slash", sbox)
    
    val p1 = manager.create("strong")
    p1.use("hello world")
    println("")

    val p2 = manager.create("warning")
    p2.use("hello world")
    println("")
    
    val p3 = manager.create("slash")
    p3.use("hello world")
  }
}