package com.designpattern.scala.ch19

/**
 * @author TC
 */
trait State {
  def doClock(context:Context, hour:Int):Unit
  def doUse(context:Context):Unit
  def doAlarm(context:Context):Unit
  def doPhone(context:Context):Unit
}