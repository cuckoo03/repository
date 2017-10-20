package com.designpattern.scala.ch16

import java.awt.Button



/**
 * @author cuckoo03
 */
class ColleagueButton(caption: String) extends Button(caption) with Colleague {
  private var mediator:Mediator = null
  def setMediator(mediator: Mediator): Unit = {
    this.mediator = mediator
  }

  def setColleagueEnabled(enabled: Boolean): Unit = {
    setEnabled(enabled)
  }
}