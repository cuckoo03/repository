package com.designpattern.scala.ch14

/**
 * @author cuckoo03
 */
class NoSupport(_name: String) extends Support(_name) {
  private val name = _name
  protected def resolve(trouble: Trouble): Boolean = false
}