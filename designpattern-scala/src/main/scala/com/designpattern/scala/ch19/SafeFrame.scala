package com.designpattern.scala.ch19

import java.awt.BorderLayout
import java.awt.Button
import java.awt.Color
import java.awt.Frame
import java.awt.TextArea
import java.awt.TextField
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.Panel

/**
 * @author TC
 */
class SafeFrame(_title: String) extends Frame with ActionListener with Context {
  private val textClock = new TextField(60)
  private val textScreen = new TextArea(10, 60)
  private val buttonUse = new Button("use")
  private val buttonAlarm = new Button("alert bell")
  private val buttonPhone = new Button("call")
  private val buttonExit = new Button("exit")

  private var currentState = DayState.getInstance()

  this.setBackground(Color.lightGray)
  this.setLayout(new BorderLayout())
  this.add(textClock, BorderLayout.NORTH)
  textClock.setEditable(false)

  add(textScreen, BorderLayout.CENTER)
  textScreen.setEnabled(false)

  val panel = new Panel()
  panel.add(buttonUse)
  panel.add(buttonAlarm)
  panel.add(buttonPhone)
  panel.add(buttonExit)

  add(panel, BorderLayout.SOUTH)

  pack()
  show()

  buttonUse.addActionListener(this)
  buttonAlarm.addActionListener(this)
  buttonPhone.addActionListener(this)
  buttonExit.addActionListener(this)

  def actionPerformed(e: ActionEvent): Unit = {
    println(e)
    if (e.getSource == buttonUse) {
      currentState.doUse(this)
    } else if (e.getSource == buttonAlarm) {
      currentState.doAlarm(this)
    } else if (e.getSource == buttonPhone) {
      currentState.doPhone(this)
    } else if (e.getSource == buttonExit) {
      System.exit(0)
    } else {
      println("?")
    }
  }

  def setClock(hour: Int): Unit = {
    var clockString = "현재시각은"
    if (hour < 10)
      clockString += "0" + hour + ":00"
    else
      clockString += hour + ":00"

    println(clockString)
    textClock.setText(clockString)
    currentState.doClock(this, hour)

  }

  def changeState(state: State): Unit = {
    println(currentState + s" to $state")
    this.currentState = state
  }

  def callSecurityCenter(msg: String): Unit = {
    textScreen.append("call " + msg + "\n")
  }

  def recordLog(msg: String): Unit = {
    textScreen.append("recored " + msg + "\n")
  }
}