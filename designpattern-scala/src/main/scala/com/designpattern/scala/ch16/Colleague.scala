package com.designpattern.scala.ch16

/**
 * @author cuckoo03
 */
trait Colleague {
 def setMediator(mediator:Mediator):Unit
 def setColleagueEnabled(enabled:Boolean):Unit
}