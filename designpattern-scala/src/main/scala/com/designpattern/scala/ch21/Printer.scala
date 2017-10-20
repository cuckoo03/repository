package com.designpattern.scala.ch21

class Printer extends Printable {
  private var name: String = null

  def this(_name: String) {
    this()
    name = _name
    heavyJob(_name)
  }

  def setPrinterName(name: String): Unit = {
    this.name = name
  }

  def getPrinterName(): String = {
    return name
  }

  def print(str: String): Unit = {
    println(s"print $name:$str")
  }

  def heavyJob(msg: String): Unit = {
    println(s"heavy job ${msg}")
    
    for (i <- 0 until 5) {
      Thread.sleep(1000)
      printf(".")
    }
    println("complete")
  }
}