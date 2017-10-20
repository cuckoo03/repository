package com.designpattern.scala.ch12

/**
 * @author cuckoo03
 */
abstract class Border extends Display {
	protected var display:Display = null
  def this(display:Display) {
    this()
    this.display = display
  }
}