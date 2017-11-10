package com.refactoring.ch06.movemethod

class Account {
  val daysOverdrawn = 0
  val _type: AccountType = new AccountType()

  def overdraftCharge(): Double = {
    return _type.overdraftCharge(daysOverdrawn)
  }

  def bankCharge(): Double = {
    if (daysOverdrawn > 0)
      _type.overdraftCharge(daysOverdrawn)
    return 0
  }
}