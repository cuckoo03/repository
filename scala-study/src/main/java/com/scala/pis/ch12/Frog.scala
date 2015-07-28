package com.scala.pis.ch12

/**
 * @author cuckoo03
 */
class Animal
class Frog extends Animal with Philosophical {
  override def toString = "green"
  
  override def philosophize() {
  	println("frog:" + toString())
  }
}