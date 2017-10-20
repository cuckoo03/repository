package com.designpattern.scala.ch14

/**
 * @author cuckoo03
 */
class LimitSupport(_name: String, _limit: Int) extends Support(_name) {
  private val limit = _limit

  def resolve(trouble: Trouble): Boolean = {
    if (trouble.getNumber() < limit) {
      true
    } else {
      false
    }
  }
}