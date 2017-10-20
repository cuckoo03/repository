package com.designpattern.scala.ch19

/**
 * @author TC
 */
trait Context {
  def setClock(hour:Int):Unit
  def changeState(state:State):Unit
  def callSecurityCenter(msg:String):Unit
  def recordLog(msg:String):Unit
}