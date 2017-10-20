package com.designpattern.scala.ch13

/**
 * @author cuckoo03
 */
trait Acceptor {
  def accept(v:Visitor):Unit
}