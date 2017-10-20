package com.designpattern.scala.ch16

import java.awt.Checkbox
import java.awt.CheckboxGroup
import java.awt.event.ItemEvent
import java.awt.event.ItemListener

/**
 * @author cuckoo03
 */
class ColleagueCheckbox(_caption: String, _group: CheckboxGroup, _state: Boolean)
    extends Checkbox(_caption, _group, _state) with ItemListener with Colleague {
  private var mediator: Mediator = null

  def setMediator(mediator: Mediator): Unit = {
    this.mediator = mediator
  }

  def setColleagueEnabled(enabled: Boolean): Unit = {
    setEnabled(enabled)
  }

  def itemStateChanged(x$1: ItemEvent): Unit = {
    mediator.colleagueChanged(this)
  }
}