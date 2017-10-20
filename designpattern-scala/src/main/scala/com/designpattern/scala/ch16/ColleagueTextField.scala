package com.designpattern.scala.ch16

import java.awt.TextField
import java.awt.event.TextListener
import java.awt.event.TextEvent
import java.awt.Color

/**
 * @author cuckoo03
 */
class ColleagueTextField(_text: String, _columns: Int)
    extends TextField(_text, _columns) with TextListener with Colleague {
  private var mediator: Mediator = null
  def setMediator(mediator: Mediator): Unit = {
    this.mediator = mediator
  }

  def setColleagueEnabled(enabled: Boolean): Unit = {
    setEnabled(enabled)
    if (enabled == true)
      setBackground(Color.white)
    else
      setBackground(Color.lightGray)
  }

  def textValueChanged(x$1: TextEvent): Unit = {
    mediator.colleagueChanged(this)
  }
}