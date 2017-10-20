package com.designpattern.scala.ch21

class PrinterProxy extends Printable {
  private var name: String = null
  private var real: Printer = null

  def this(_name: String) {
    this()
    this.name = _name
  }

  def setPrinterName(name: String): Unit = {
    synchronized {
      if (real != null)
        real.setPrinterName(name)

      this.name = name
    }
  }

  def getPrinterName(): String = {
    name
  }

  def print(string: String): Unit = {
    realize()
    real.print(string)
  }

  private def realize(): Unit = {
    this.synchronized {
      if (real == null)
        real = new Printer(name)
    }
  }
}