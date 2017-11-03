package com.refactoring.ch01

import scala.beans.BeanProperty

class Movie(private val _title: String, private val _priceCode: Int) {
  @BeanProperty
  val title = _title
//  val priceCode = _priceCode
  private var price:Price = _

  def getPriceCode():Int = price.getPriceCode()
  def getCharge(daysRented: Int): Double = {
    return price.getCharge(daysRented)
  }
  def setPriceCode(genre: Int): Unit = {
    var result: Double = 0
    genre match {
      case Movie.REGULAR => {
        price = new RegularPrice()
      }
      case Movie.NEW_RELEASE => {
        price = new NewReleasePrice()
      }
      case Movie.CHILDREN => {
        price = new ChildrenPrice()
      }
      case _ => throw new IllegalArgumentException("incorrect")
    }
  }
  def getFrequentRenterPoints(daysRented: Int): Int = {
    price.getFrequentRenterPoints(daysRented)
  }
}
object Movie {
  val CHILDREN = 2
  val REGULAR = 0
  val NEW_RELEASE = 1
}