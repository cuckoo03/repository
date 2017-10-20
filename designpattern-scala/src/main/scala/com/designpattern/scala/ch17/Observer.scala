package com.designpattern.scala.ch17

/**
 * @author cuckoo03
 */
trait Observer {
  def update(generator:NumberGenerator):Unit
}