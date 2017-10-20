package com.designpattern.scala.ch02.inheritance

/**
 * @author cuckoo03
 */
class PrintBanner(var banner1: String) extends Banner(banner1) with Print {
  def printStrong: Unit = {
    showWithAster()
  }

  def printWeak: Unit = {
    showWithParen()
  }
}