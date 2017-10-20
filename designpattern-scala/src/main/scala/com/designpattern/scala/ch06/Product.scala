package com.designpattern.scala.ch06

/**
 * @author cuckoo03
 */
trait Product extends Cloneable {
  def use(s:String):Unit
  def createClone():Product
}