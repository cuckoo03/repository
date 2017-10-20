package com.designpattern.scala.ch22

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFrame

/**
 * @author TC
 */
class Main(_title: String) extends JFrame(_title)
    with ActionListener with MouseMotionListener with WindowListener {
  private val history = new MacroCommand()
  private val canvas = new DrawCanvas(400, 400, history)
  private val clearButton = new JButton("clear")

  this.addWindowListener(this)
  canvas.addMouseMotionListener(this)
  clearButton.addActionListener(this)

  val buttonBox = new Box(BoxLayout.X_AXIS)
  buttonBox.add(clearButton)

  val mainBox = new Box(BoxLayout.Y_AXIS)
  mainBox.add(buttonBox)
  mainBox.add(canvas)
  getContentPane().add(mainBox)

  pack()
  setVisible(true)

  def actionPerformed(e: ActionEvent): Unit = {
    e.getSource match {
      case clearButton => {
        history.clear()
        canvas.repaint()
      }
    }
  }

  def mouseMoved(x$1: MouseEvent): Unit = {
  }

  def mouseDragged(e: MouseEvent): Unit = {
	  val cmd = new DrawCommand(canvas, e.getPoint)
    history.append(cmd)
    cmd.execute()
  }

  def windowClosing(e: WindowEvent): Unit = {
    System.exit(0)
  }

  def windowActivated(x$1: WindowEvent): Unit = {
  }

  def windowClosed(x$1: WindowEvent): Unit = {
  }

  def windowDeactivated(x$1: WindowEvent): Unit = {
  }

  def windowDeiconified(x$1: WindowEvent): Unit = {
  }

  def windowIconified(x$1: WindowEvent): Unit = {
  }

  def windowOpened(x$1: WindowEvent): Unit = {
  }
}