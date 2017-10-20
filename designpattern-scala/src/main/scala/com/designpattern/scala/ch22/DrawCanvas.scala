package com.designpattern.scala.ch22

import java.awt.Canvas
import java.awt.Color
import java.awt.Graphics

/**
 * @author TC
 */
class DrawCanvas(_width: Int, _height: Int, _history: MacroCommand)
    extends Canvas with Drawable {
  private val history = _history
  private var color = Color.red
  private val radius = 6

  setSize(_width, _height)
  setBackground(Color.white)

  override def paint(g: Graphics): Unit = {
    history.execute()
  }

  def draw(x: Int, y: Int): Unit = {
    val g = getGraphics
    g.setColor(color)
    g.fillOval(x - radius, y - radius, radius * 2, radius * 2)
  }

  def setColor(color: Color): Unit = {
    this.color = color
  }
}