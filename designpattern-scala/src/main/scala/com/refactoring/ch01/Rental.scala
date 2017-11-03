package com.refactoring.ch01

import scala.beans.BeanProperty

class Rental(@BeanProperty val movie: Movie, @BeanProperty val daysRented: Int) {
  def getCharge(): Double = {
    movie.getCharge(daysRented)
  }
  def getFrequentRenterPoints(): Int = {
    movie.getFrequentRenterPoints(daysRented)
  }
}