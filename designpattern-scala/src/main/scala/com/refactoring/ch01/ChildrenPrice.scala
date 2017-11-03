package com.refactoring.ch01

class ChildrenPrice extends Price {
  def getPriceCode(): Int = {
    Movie.CHILDREN
  }
  def getCharge(daysRented: Int): Double = {
    var result: Double = 1.5
    if (daysRented > 3)
      result += (daysRented - 3) * 1.5
    result
  }
}