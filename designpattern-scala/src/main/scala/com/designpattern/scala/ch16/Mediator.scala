package com.designpattern.scala.ch16

/**
 * @author cuckoo03
 */
trait Mediator {
  def createColleagues(): Unit
  def colleagueChanged(colleague: Colleague): Unit
}