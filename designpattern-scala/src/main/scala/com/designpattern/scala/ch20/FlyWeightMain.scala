package com.designpattern.scala.ch20

object FlyWeightMain {
  def main(args: Array[String]) {
    val fileNames = "aba"
    val bs = new BigString(fileNames)
    bs.print()
  }
}