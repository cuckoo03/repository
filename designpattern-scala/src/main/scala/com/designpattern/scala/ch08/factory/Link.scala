package com.designpattern.scala.ch08.factory

/**
 * @author cuckoo03
 */
abstract class Link(caption:String, _url:String) extends Item(caption) {
  protected val url:String = _url
}