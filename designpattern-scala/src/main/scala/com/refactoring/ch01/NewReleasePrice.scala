package com.refactoring.ch01

class NewReleasePrice extends Price {
  def getPriceCode(): Int = {
    Movie.NEW_RELEASE
  }
  def getCharge(daysRented: Int): Double = {
    daysRented * 3
  }
  override def getFrequentRenterPoints(daysRented: Int): Int = {
    if (daysRented > 1)
      2
    else
      1
  }
}