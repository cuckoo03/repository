package com.designpattern.scala.ch21

trait Printable {
   def setPrinterName(name:String):Unit 
   def getPrinterName():String
   def print(string:String):Unit
}