package com.designpattern.scala.ch14

/**
 * @author cuckoo03
 */
abstract class Support(_name: String) {
  private val name = _name
  private var next: Support = null
  def setNext(next: Support): Support = {
    this.next = next
    return next
  }
  final def support(trouble: Trouble): Unit = {
    if (resolve(trouble)) {
      done(trouble)
    } else if (next != null) {
      next.support(trouble)
    } else {
      fail(trouble)
    }
  }
  override def toString(): String = s"[$name]"
  protected def resolve(trouble: Trouble): Boolean
  protected def done(trouble: Trouble): Unit = {
    println(s"$trouble is resolved by $this")
  }
  protected def fail(trouble: Trouble): Unit = {
    println(s"$trouble cannot be resolved")
  }
}