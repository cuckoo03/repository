package com.designpattern.scala.ch02.delegate

/**
 * @author cuckoo03
 */
class PrintBanner(private val _banner:String) extends Print {
  private var banner:Banner = new Banner(_banner) 

  def printStrong: Unit = {
    banner.showWithAster()
  }

  def printWeak: Unit = {
    banner.showWithParen()
  }
}