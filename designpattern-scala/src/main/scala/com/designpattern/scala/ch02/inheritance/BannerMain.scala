package com.designpattern.scala.ch02.inheritance

/**
 * @author cuckoo03
 */
object BannerMain {
  def main(args:Array[String]) {
    val p = new PrintBanner("Hello")
    p.printWeak
    p.printStrong
  }
}