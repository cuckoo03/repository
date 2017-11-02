package com.refactoring.ch01

import scala.beans.BeanProperty

@BeanProperty
case class Rental(private val movie:Movie, private val daysRented:Int) {
  
}