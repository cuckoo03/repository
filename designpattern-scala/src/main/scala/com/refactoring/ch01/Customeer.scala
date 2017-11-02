package com.refactoring.ch01

import scala.collection.mutable.ListBuffer

class Customeer(private val name: String) {
  val rentals: ListBuffer[Rental] = ListBuffer.empty[Rental]
  def getName(): String = {
    return name
  }

  def statement(): Unit = {
    val totalAmount = 0
    val frequentReenterPoints = 0
    rentals.foreach { each =>
      val thisAmount = 0
    }
  }
}