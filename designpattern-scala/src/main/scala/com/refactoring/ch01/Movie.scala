package com.refactoring.ch01

import scala.beans.BeanProperty

@BeanProperty
class Movie(private val title: String, private val priceeCode: Int) {

}
object Movie {
  val CHILDREN: Int = 2
}