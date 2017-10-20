package com.designpattern.scala.ch14

/**
 * @author cuckoo03
 */
class OddSupport(_name: String) extends Support(_name) {
  def resolve(trouble: Trouble): Boolean = {
    if (trouble.getNumber() % 2 == 1) {
      true
    } else {
      false
    }
  }
}