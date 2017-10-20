package com.designpattern.scala.ch08.factory

/**
 * @author cuckoo03
 */
abstract class Item(_caption:String) {
  protected val caption:String = _caption
  def makeHTML():String
}