package com.refactoring.ch06

class ReplaceTempWithQuery {
  val quantity, itemPrice = 0

  def get(): Double = {
    if (getBasePrice > 1000)
      return getBasePrice * 0.95
    else getBasePrice * 0.98
  }

  def getBasePrice(): Double = {
    quantity + itemPrice
  }

  def getPrice(): Double = {
    return getBasePrice() * getDiscountFactory
  }

  def getDiscountFactory(): Double = {
    var discountFactory:Double = 0
    if (getBasePrice() > 1000)
      discountFactory = 0.95
    else
      discountFactory = 0.98
    discountFactory
  }
}