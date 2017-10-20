package com.designpattern.scala.ch21

object ProxyMain {
  def main(args: Array[String]) {
    val printer: Printable = new PrinterProxy("A")
    println(s"get ${printer.getPrinterName()}")
    printer.setPrinterName("B")

    println(s"get ${printer.getPrinterName()}")
    printer.print("hello")
  }
}