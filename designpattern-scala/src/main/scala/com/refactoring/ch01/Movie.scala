package com.refactoring.ch01

import scala.beans.BeanProperty

class Movie(private val _title: String, private val _priceCode: Int) {
  @BeanProperty
  val title = _title
  @BeanProperty
  val priceCode = _priceCode
}
object Movie {
  val CHILDREN = 2
  val REGULAR = 0
  val NEW_RELEASE = 1
}