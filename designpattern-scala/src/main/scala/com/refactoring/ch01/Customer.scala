package com.refactoring.ch01

import scala.collection.mutable.ListBuffer

class Customeer(private val name: String) {
  val rentals: ListBuffer[Rental] = ListBuffer.empty[Rental]
  def getName(): String = {
    return name
  }

  def statement(): String = {
    var totalAmount: Double = 0
    var frequentReenterPoints: Int = 0
    var result = s"rental record for $getName()\n"
    rentals.foreach { each =>
      frequentReenterPoints += each.getFrequentRenterPoints()

      result += s"\t ${each.getMovie().getTitle()}\t ${each.getCharge()}\n"
      totalAmount += each.getCharge()
    }

    result += s"amount owed is ${getTotalCharge()}\n"
    result += s"you earned $frequentReenterPoints frequent renter points"
    return result
  }
  def getTotalCharge(): Double = {
    var result: Double = 0
    rentals.foreach { each =>
      result += each.getCharge()
    }

    result
  }
}