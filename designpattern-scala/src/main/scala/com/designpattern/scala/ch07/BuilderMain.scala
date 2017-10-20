package com.designpattern.scala.ch07

/**
 * @author cuckoo03
 */
object BuilderMain {
  def main(args: Array[String]) {
    val director = new Director(new TextBuilder())
    println(director.construct())
    
    println("\n")
    
    val director2 = new Director(new SimpleBuilder) 
    println(director2.construct())
  }
}