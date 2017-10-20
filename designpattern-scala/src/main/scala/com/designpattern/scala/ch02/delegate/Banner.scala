package com.designpattern.scala.ch02.delegate

/**
 * @author cuckoo03
 */
class Banner(var banner: String) {

  def showWithParen(): Unit = {
    println("( s$banner )")
  }
  def showWithAster(): Unit = {
    println("* s$banner *")
  }
}