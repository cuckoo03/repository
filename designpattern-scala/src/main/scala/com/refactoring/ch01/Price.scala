package com.refactoring.ch01

abstract class Price {
  def getPriceCode(): Int
  def getCharge(daysRented: Int): Double
  def getFrequentRenterPoints(daysRented: Int): Int = {
    1
  }
}