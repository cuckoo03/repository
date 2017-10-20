package com.designpattern.scala.ch14

/**
 * @author cuckoo03
 */
class Trouble(_number: Int) {
  private val number = _number
  def getNumber(): Int = return number
  override def toString(): String = s"[Trouble $number]"
}