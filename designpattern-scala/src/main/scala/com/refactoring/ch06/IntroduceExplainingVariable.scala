package com.refactoring.ch06

class IntroduceExplainingVariable {
  val quantity = 0
  val itemPrice = 0

  def price(): Double = {
    val basePrice = quantity * itemPrice
    val quantityDiscount = Math.max(0, quantity - 500) * itemPrice * 0.05
    val shipping = Math.min(basePrice * 0.1, 100)
    return basePrice - quantityDiscount + shipping 
  }
}