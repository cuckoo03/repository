package com.designpattern.scala.ch04

/**
 * @author cuckoo03
 */
class IDCard(private var owner: String) extends Product {
  override def use(): Unit = {
    println(s"$owner 의 카드를 사용")
  }
  def getOwner(): String = owner
}