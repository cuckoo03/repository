package com.designpattern.scala.ch14

/**
 * @author cuckoo03
 */
class SpecialSupport(_name: String, _number: Int) extends Support(_name) {
  private val number = _number
  def resolve(trouble: Trouble): Boolean = {
    if (trouble.getNumber() == number) {
      true
    } else {
      false
    }
  }
}