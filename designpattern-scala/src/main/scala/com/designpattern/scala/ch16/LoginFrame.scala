package com.designpattern.scala.ch16

import java.awt.Frame
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.awt.Color
import java.awt.GridLayout
import java.awt.Label
import java.awt.CheckboxGroup

/**
 * @author cuckoo03
 */
class LoginFrame(_title: String) extends Frame(_title)
    with ActionListener with Mediator {
  private var checkGuest: ColleagueCheckbox = null
  private var checkLogin: ColleagueCheckbox = null
  private var textUser: ColleagueTextField = null
  private var textPass: ColleagueTextField = null
  private var buttonOk: ColleagueButton = null
  private var buttonCancel: ColleagueButton = null

  setBackground(Color.lightGray)
  setLayout(new GridLayout(4, 2))

  createColleagues()

  add(checkGuest)
  add(checkLogin)
  add(new Label("username:"))
  add(textUser)
  add(new Label("password:"))
  add(textPass)
  add(buttonOk)
  add(buttonCancel)

  colleagueChanged(checkGuest)
  
  pack()
  show()

  def actionPerformed(e: ActionEvent): Unit = {
    println(e)
    System.exit(0)
  }

  def createColleagues(): Unit = {
    val g = new CheckboxGroup()
    checkGuest = new ColleagueCheckbox("guest", g, true)
    checkLogin = new ColleagueCheckbox("login", g, false)
    textUser = new ColleagueTextField("", 10)
    textPass = new ColleagueTextField("", 10)
    textPass.setEchoChar('*')
    buttonOk = new ColleagueButton("ok")
    buttonCancel = new ColleagueButton("cancel")

    checkGuest.setMediator(this)
    checkLogin.setMediator(this)
    textUser.setMediator(this)
    textPass.setMediator(this)
    buttonOk.setMediator(this)
    buttonCancel.setMediator(this)

    checkGuest.addItemListener(checkGuest)
    checkGuest.addItemListener(checkLogin)
    textUser.addTextListener(textUser)
    textPass.addTextListener(textPass)
    buttonOk.addActionListener(this)
    buttonCancel.addActionListener(this)
  }

  def colleagueChanged(c: Colleague): Unit = {
    if (c == checkGuest || c == checkLogin) {
      if (checkGuest.getState) {
        textUser.setColleagueEnabled(false)
        textPass.setColleagueEnabled(false)
        buttonOk.setColleagueEnabled(true)
      } else {
        textUser.setColleagueEnabled(true)
        userpassChanged()
      }
    } else if (c == textUser || c == textPass) {
      userpassChanged()
    } else {
      println("colleagueChanged:" + c)
    }
  }

  private def userpassChanged(): Unit = {
    if (textUser.getText.length() > 0) {
      textPass.setColleagueEnabled(true)
      if (textPass.getText.length() > 0) {
        buttonOk.setColleagueEnabled(true)
      } else {
        buttonOk.setColleagueEnabled(false)
      }
    } else {
      textPass.setColleagueEnabled(false)
      buttonOk.setColleagueEnabled(false)
    }
  }
}