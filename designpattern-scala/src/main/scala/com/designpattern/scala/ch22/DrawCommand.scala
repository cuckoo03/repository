package com.designpattern.scala.ch22

import java.awt.Point

/**
 * @author TC
 */
class DrawCommand(_drawable: Drawable, _point: Point) extends Command {
  protected val drawable = _drawable
  private val position = _point

  def execute(): Unit = {
    drawable.draw(position.x, position.y)
  }
}