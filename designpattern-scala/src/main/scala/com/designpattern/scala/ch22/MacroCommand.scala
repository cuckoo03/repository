package com.designpattern.scala.ch22

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Stack

/**
 * @author TC
 */
class MacroCommand extends Command {
  private val commands = new Stack[Command]()

  def execute(): Unit = {
    commands.foreach {
      _.execute()
    }
  }

  def append(cmd: Command): Unit = {
    if (cmd != null)
      commands.push(cmd)
  }

  def undo(): Unit = {
    if (!commands.isEmpty) {
      commands.pop()
    }
  }
  
  def clear():Unit = {
    commands.clear()
  }
}