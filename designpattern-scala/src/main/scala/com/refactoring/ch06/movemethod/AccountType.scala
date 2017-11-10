package com.refactoring.ch06.movemethod

class AccountType {
  def isPremium(): Boolean = false

  def overdraftCharge(daysOverdrawn: Int): Double = {
    if (isPremium())
      1
    return daysOverdrawn
  }
}