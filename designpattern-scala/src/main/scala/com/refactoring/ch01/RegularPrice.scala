package com.refactoring.ch01

class RegularPrice extends Price {
  def getPriceCode(): Int = {
    Movie.REGULAR
  }
  def getCharge(daysRented: Int): Double = {
    var result: Double = 2
    if (daysRented > 2)
      result += (daysRented - 2) * 1.5
    result
  }
}