package com.designpattern.scala.ch20

import java.io.IOException
import java.io.BufferedReader
import java.io.FileReader
import scala.io.Source

class BigChar(_charName: String) {
  private val charName = _charName
  private var fontData: String = null

  try {
    var lines = ""
    Source.fromFile("big" + charName + ".txt").foreach {
      lines += _
    }
    this.fontData = lines
  } catch {
    case e: IOException => {
      fontData = charName + "?"
    }
  }

  def print(): Unit = println(fontData)
  
  override def toString():String = {
    fontData
  }
}