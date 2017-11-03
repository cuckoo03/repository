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
    rentals.foreach { rental =>
      var thisAmount: Double = 0

      thisAmount = rental.getCharge()

      frequentReenterPoints += 1
      if ((rental.getMovie().getPriceCode() == Movie.NEW_RELEASE) &&
        rental.getDaysRented() > 1)
        frequentReenterPoints += 1

      result += s"\t ${rental.getMovie().getTitle()}\t $thisAmount\n"
      totalAmount += thisAmount
    }

    result += s"amount owed is $totalAmount\n"
    result += s"you earned $frequentReenterPoints frequent renter points"
    return result
  }
}