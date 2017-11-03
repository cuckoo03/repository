package com.refactoring.ch01

import scala.beans.BeanProperty

class Rental(private val _movie: Movie, private val _daysRented: Int) {
  @BeanProperty
  val movie = _movie
  @BeanProperty
  val daysRented = _daysRented

  def getCharge(): Double = {
    var result: Double = 0
    getMovie().getPriceCode() match {
      case Movie.REGULAR => {
        result += 2
        if (getDaysRented() > 2)
          result += (getDaysRented() - 2) * 1.5
      }
      case Movie.NEW_RELEASE => {
        result += getDaysRented() * 3
      }
      case Movie.CHILDREN => {
        result += 1.5
        if (getDaysRented() > 3)
          result += (getDaysRented() - 3) * 1.5
      }
    }
    return result
  }
}